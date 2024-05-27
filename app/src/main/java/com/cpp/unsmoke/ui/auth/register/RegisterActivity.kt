package com.cpp.unsmoke.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.databinding.ActivityRegisterBinding
import com.cpp.unsmoke.ui.auth.login.LoginActivity
import com.cpp.unsmoke.ui.auth.login.LoginViewModel
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedActivity
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ObtainViewModelFactory.obtain<RegisterViewModel>(this)

        supportActionBar?.hide()

        setUpSpannableTv()
        hintSetup()

        binding.btnSignup.setOnClickListener {
            val fullName = binding.edtSignupName.text.toString()
            val email = binding.edtSignupEmail.text.toString()
            val password = binding.signupEdtPassword.text.toString()
            val confirmPassword = binding.signupEdtConfirmPassword.text.toString()

            val isEmpty = isInputEmpty(fullName, email, password, confirmPassword)
            if (isEmpty) {
                if (fullName.isEmpty()) {
                    binding.signupNameEditTextLayout.error = "Field Cannot Be Empty"
                }
                if (email.isEmpty()) {
                    binding.signupEmailEditTextLayout.error = "Field Cannot Be Empty"
                }
                if (password.isEmpty()) {
                    binding.signupPasswordEditTextLayout.error = "Field Cannot Be Empty"
                }
                if (confirmPassword.isEmpty()) {
                    binding.signupConfirmPasswordEditTextLayout.error = "Field Cannot Be Empty"
                }
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.signupConfirmPasswordEditTextLayout.error = "Passwords do not match"
                return@setOnClickListener
            }

            if (areErrorsNull(
                    binding.signupNameEditTextLayout,
                    binding.signupEmailEditTextLayout,
                    binding.signupPasswordEditTextLayout,
                    binding.signupConfirmPasswordEditTextLayout
                )) {
                viewModel.register(fullName, email, password).observe(this){result ->
                    if (result != null){
                        when(result){
                            is Result.Loading -> {
                                binding.btnSignup.isClickable = false
                            }
                            is Result.Error -> {
                                if (result.error.contains("Email")){
                                    binding.signupEmailEditTextLayout.error = result.error
                                } else {
                                    binding.signupNameEditTextLayout.error = result.error
                                    binding.signupEmailEditTextLayout.error = result.error
                                    binding.signupPasswordEditTextLayout.error = result.error
                                    binding.signupConfirmPasswordEditTextLayout.error = result.error
                                }
                                binding.btnLogin.isClickable = true
                            }
                            is Result.Success -> {
                                binding.btnLogin.isClickable = true
                                toPersonalized()
                            }

                            else -> {}
                        }
                    }

                }
            }
        }

        binding.btnLogin.setOnClickListener {
            toPersonalized()
        }
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toPersonalized() {
        val intent = Intent(this, PersonalizedActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun hintSetup() {
        binding.signupNameEditTextLayout.setHint(R.string.full_name)
        binding.signupEmailEditTextLayout.setHint(R.string.email)
        binding.signupPasswordEditTextLayout.setHint(R.string.password)
        binding.signupConfirmPasswordEditTextLayout.setHint(R.string.password)
    }

    private fun setUpSpannableTv() {
        val spannableString = SpannableString(
            "By signing up, I agree to the terms & conditions of the Unsmoke app."
        )

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@RegisterActivity, "This Feature is available soon", Toast.LENGTH_SHORT).show()
            }
        }

        spannableString.setSpan(clickableSpan, 30, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.dark_green)), 30, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.textView.text = spannableString
        binding.textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun isInputEmpty(vararg inputs: String): Boolean{
        return inputs.any { it.isEmpty() }
    }

    private fun areErrorsNull(vararg textInputLayouts: TextInputLayout): Boolean {
        return textInputLayouts.all { it.error == null }
    }
}
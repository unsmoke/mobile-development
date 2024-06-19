package com.cpp.unsmoke.ui.auth.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.databinding.ActivityLoginBinding
import com.cpp.unsmoke.ui.auth.forgotpassword.ForgotPasswordActivity
import com.cpp.unsmoke.ui.auth.register.RegisterActivity
import com.cpp.unsmoke.ui.main.MainActivity
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedActivity
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ObtainViewModelFactory.obtainAuth<LoginViewModel>(this)

        lifecycleScope.launch {
            val isLogin = viewModel.getLoginStatus().first()
            if (isLogin) {
                toHome()
            }
        }

        val alertBuilder = AlertDialog.Builder(this)

        supportActionBar?.hide()

        hintSetup()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtLoginEmail.text.toString()
            val password = binding.edtLoginPassword.text.toString()

            val emailError = binding.loginEmailEditTextLayout.error
            val passwordError = binding.loginPasswordEditTextLayout.error

            if (email.isEmpty() || password.isEmpty() ){
                binding.loginEmailEditTextLayout.error = "Field Cannot Empty"
                binding.loginPasswordEditTextLayout.error = "Field Cannot Empty"
            }

            if (emailError == null && passwordError == null && email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password).observe(this){result ->
                    if (result != null){
                        when(result){
                            is Result.Loading -> {
                                binding.btnLogin.isClickable = false
                            }
                            is Result.Error -> {
                                binding.loginEmailEditTextLayout.error = result.error
                                binding.loginPasswordEditTextLayout.error = result.error
                                binding.btnLogin.isClickable = true
                            }
                            is Result.Success -> {
                                val personalizedViewModel = ObtainViewModelFactory.obtainAuth<PersonalizedViewModel>(this)
                                personalizedViewModel.getPersonalizedPlan().observe(this){resultPlan ->
                                    if (resultPlan != null){
                                        when(resultPlan){
                                            is Result.Loading -> {
                                                binding.btnLogin.isClickable = false
                                            }
                                            is Result.Error -> {
                                                if(resultPlan.error.contains("not found")){
                                                    toPersonalized()
                                                }

                                                if(resultPlan.error.contains("not authorized")) {
                                                    viewModel.logout()
                                                    alertBuilder.setTitle("Unauthorized")
                                                    alertBuilder.setMessage("Please Login Again")
                                                    alertBuilder.setPositiveButton("OK") { _, _ ->
                                                        val intent =
                                                            Intent(this, LoginActivity::class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                                    alertBuilder.show()
                                                } else {
                                                    binding.loginEmailEditTextLayout.error = resultPlan.error
                                                    binding.loginPasswordEditTextLayout.error = resultPlan.error
                                                    binding.btnLogin.isClickable = true
                                                }
                                            }
                                            is Result.Success -> {
                                                binding.btnLogin.isClickable = true
                                                toHome()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.btnSignup.setOnClickListener {
            toSignUp()
        }

        binding.btnForgotPassword.setOnClickListener {
            toForgotPassword()
        }

        binding.btnSigninWithGoogle.setOnClickListener {
            toSignInWithGoogle()
        }
    }

    private fun toPersonalized() {
        val intent = Intent(this, PersonalizedActivity::class.java)
        startActivity(intent)
    }

    private fun hintSetup() {
        binding.loginEmailEditTextLayout.setHint(R.string.email)
        binding.loginPasswordEditTextLayout.setHint(R.string.password)
    }

    private fun toSignInWithGoogle() {
        Toast.makeText(this, "This Feature is available soon", Toast.LENGTH_SHORT).show()
    }

    private fun toForgotPassword() {
        val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun toSignUp() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun toHome() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
//        Toast.makeText(this, "This Feature is available soon", Toast.LENGTH_SHORT).show()
    }
}
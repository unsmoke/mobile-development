package com.cpp.unsmoke.ui.auth.newpassword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityNewPasswordBinding
import com.cpp.unsmoke.ui.auth.login.LoginActivity
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class NewPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPasswordBinding
    private var emailValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ObtainViewModelFactory.obtainAuth<NewPasswordViewModel>(this)

        hintSetup()

        setSupportActionBar(binding.newPasswordToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left)

        val email = intent.getStringExtra("email")

        if (email != null) {
            viewModel.setEmail(email)
        }

        viewModel.email.observe(this) {
            emailValue = it
        }

        binding.btnSave.setOnClickListener {
            Log.d("NewPasswordActivity", "onCreate: Save Button Clicked")
            val otpCode = binding.edtOtp.text.toString()
            val newPassword = binding.edtNewPassword.text.toString()
            val confirmNewPassword = binding.edtConfirmNewPassword.text.toString()

            if (newPassword.isEmpty() || confirmNewPassword.isEmpty() ) {
                Log.d("NewPasswordActivity", "onCreate: Empty Field")
                if (newPassword.isEmpty()) {
                    binding.newPasswordEditTextLayout.error = "Field Cannot Be Empty"
                } else {
                    binding.newPasswordEditTextLayout.isErrorEnabled = false
                }
                if (confirmNewPassword.isEmpty()) {
                    binding.confirmNewPasswordEditTextLayout.error = "Field Cannot Be Empty"
                }
                return@setOnClickListener
            }

            if(!validateOtpCode(otpCode)){
                Log.d("NewPasswordActivity", "onCreate: OTP Code Valid")
                return@setOnClickListener
            }

            if (newPassword != confirmNewPassword) {
                binding.confirmNewPasswordEditTextLayout.error = "Passwords do not match"
                return@setOnClickListener
            }

            viewModel.newPassword(emailValue, newPassword, otpCode).observe(this) { result ->
                when (result) {
                    is com.cpp.unsmoke.data.remote.Result.Loading -> {
                        binding.newPasswordProgressBar.visibility = android.view.View.VISIBLE
                    }

                    is com.cpp.unsmoke.data.remote.Result.Success -> {
                        binding.newPasswordProgressBar.visibility = android.view.View.GONE
                        toLogin()
                    }

                    is com.cpp.unsmoke.data.remote.Result.Error -> {
                        binding.newPasswordProgressBar.visibility = android.view.View.GONE
                        android.widget.Toast.makeText(this, result.error, android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateOtpCode(otpCode: String): Boolean {
        return when {
            otpCode.isEmpty() -> {
                binding.otpEditTextLayout.error = "OTP cannot be empty"
                false
            }
            otpCode.length != 6 -> {
                binding.otpEditTextLayout.error = "OTP must be 6 digits"
                false
            }
            else -> {
                binding.otpEditTextLayout.error = null
                true
            }
        }
    }

    private fun toLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun hintSetup() {
        binding.newPasswordEditTextLayout.setHint(R.string.new_password)
        binding.confirmNewPasswordEditTextLayout.setHint(R.string.new_password)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
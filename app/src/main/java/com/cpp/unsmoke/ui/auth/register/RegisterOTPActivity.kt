package com.cpp.unsmoke.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityRegisterOtpactivityBinding
import com.cpp.unsmoke.ui.auth.login.LoginActivity
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedActivity
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class RegisterOTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterOtpactivityBinding
    private var emailValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ObtainViewModelFactory.obtainAuth<RegisterViewModel>(this)

        val email = intent.getStringExtra("Email")

        if (email != null) {
            viewModel.setEmail(email)
        }

        viewModel.email.observe(this) {
            emailValue = it
        }

        hintSetup()

        binding.btnVerify.setOnClickListener {
            val otpCode = binding.edtOtpRegister.text.toString()
            if (validateOtpCode(otpCode)) {
                Log.d("RegisterOTPActivity", "Email: $emailValue")
                viewModel.verifyEmail(emailValue, otpCode).observe(this) { result ->
                    when (result) {
                        is com.cpp.unsmoke.data.remote.Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is com.cpp.unsmoke.data.remote.Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        is com.cpp.unsmoke.data.remote.Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun hintSetup() {
        binding.edtOtpRegister.setHint("Otp Code")
    }

    private fun validateOtpCode(otpCode: String): Boolean {
        return when {
            otpCode.isEmpty() -> {
                binding.forgotPasswordEditTextLayout.error = "OTP cannot be empty"
                false
            }
            otpCode.length != 6 -> {
                binding.forgotPasswordEditTextLayout.error = "OTP must be 6 digits"
                false
            }
            else -> {
                binding.forgotPasswordEditTextLayout.error = null
                true
            }
        }
    }
}
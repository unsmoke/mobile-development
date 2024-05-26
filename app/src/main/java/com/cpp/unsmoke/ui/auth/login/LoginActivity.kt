package com.cpp.unsmoke.ui.auth.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.databinding.ActivityLoginBinding
import com.cpp.unsmoke.ui.auth.forgotpassword.ForgotPasswordActivity
import com.cpp.unsmoke.ui.auth.register.RegisterActivity
import com.cpp.unsmoke.ui.main.MainActivity
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ObtainViewModelFactory.obtain<LoginViewModel>(this)

        val alertBuilder = AlertDialog.Builder(this)

        supportActionBar?.hide()

        hintSetup()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtLoginEmail.text.toString()
            val password = binding.edtLoginPassword.text.toString()
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
                            binding.btnLogin.isClickable = true
                            toHome()
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
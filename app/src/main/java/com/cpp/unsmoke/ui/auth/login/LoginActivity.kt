package com.cpp.unsmoke.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cpp.unsmoke.databinding.ActivityLoginBinding
import com.cpp.unsmoke.ui.auth.forgotpassword.ForgotPasswordActivity
import com.cpp.unsmoke.ui.auth.register.RegisterActivity
import com.cpp.unsmoke.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {
            toHome()
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

    private fun toSignInWithGoogle() {
        TODO("Not yet implemented")
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
        TODO("Add Finish Method In Future")
    }
}
package com.cpp.unsmoke.ui.auth.forgotpassword

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityForgotPasswordBinding
import com.cpp.unsmoke.ui.auth.newpassword.NewPasswordActivity

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hintSetup()

        setSupportActionBar(binding.forgotPasswordToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left)

        binding.btnSend.setOnClickListener {
            sendEmail()
        }
    }

    private fun sendEmail() {
        val intent = Intent(this, NewPasswordActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun hintSetup() {
        binding.forgotPasswordEditTextLayout.setHint(R.string.email)
    }
}
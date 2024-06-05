package com.cpp.unsmoke.ui.auth.newpassword

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityNewPasswordBinding
import com.cpp.unsmoke.ui.auth.login.LoginActivity

class NewPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hintSetup()

        setSupportActionBar(binding.newPasswordToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left)

        binding.btnSave.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
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
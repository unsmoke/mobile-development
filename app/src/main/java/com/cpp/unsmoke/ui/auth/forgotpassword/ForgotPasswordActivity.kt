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
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ObtainViewModelFactory.obtainAuth<ForgotPasswordViewModel>(this)

        hintSetup()

        setSupportActionBar(binding.forgotPasswordToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left)

        binding.btnSend.setOnClickListener {
            val email = binding.edtForgotPassword.text.toString()
            viewModel.forgotPassword(email).observe(this) { result ->
                when (result) {
                    is com.cpp.unsmoke.data.remote.Result.Loading -> {
                        binding.forgotPasswordProgressBar.visibility = android.view.View.VISIBLE
                    }

                    is com.cpp.unsmoke.data.remote.Result.Success -> {
                        binding.forgotPasswordProgressBar.visibility = android.view.View.GONE
                        sendEmail(email)
                    }

                    is com.cpp.unsmoke.data.remote.Result.Error -> {
                        binding.forgotPasswordProgressBar.visibility = android.view.View.GONE
                        android.widget.Toast.makeText(this, result.error, android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun sendEmail(email: String) {
        val intent = Intent(this, NewPasswordActivity::class.java).apply{
            putExtra("email", email)
        }
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
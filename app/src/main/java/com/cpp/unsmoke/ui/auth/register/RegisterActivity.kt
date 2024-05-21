package com.cpp.unsmoke.ui.auth.register

import android.os.Bundle
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
import com.cpp.unsmoke.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpSpannableTv()
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
}
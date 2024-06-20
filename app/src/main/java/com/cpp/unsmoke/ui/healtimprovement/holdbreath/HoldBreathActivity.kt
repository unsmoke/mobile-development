package com.cpp.unsmoke.ui.healtimprovement.holdbreath

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityHoldBreathBinding

class HoldBreathActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHoldBreathBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoldBreathBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
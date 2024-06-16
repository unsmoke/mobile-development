package com.cpp.unsmoke.ui.ismoke

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityIsmokeBinding

class IsmokeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIsmokeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIsmokeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.ismokeToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
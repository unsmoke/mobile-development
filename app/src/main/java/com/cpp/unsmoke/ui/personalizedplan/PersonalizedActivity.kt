package com.cpp.unsmoke.ui.personalizedplan

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityPersonalizedBinding

class PersonalizedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalizedBinding
    private lateinit var viewModel: PersonalizedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalizedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.personalizedToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left)

        viewModel = ViewModelProvider(this)[PersonalizedViewModel::class.java]

        viewModel.currentProgress.observe(this){
            setProgressWithAnimation(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        viewModel.currentProgress.observe(this){
            setProgressWithAnimation(it)
        }
    }

    private fun setProgressWithAnimation(progress: Int) {
        val animation = ObjectAnimator.ofInt(binding.progressBar, "progress", binding.progressBar.progress, progress)
        animation.duration = 500 // duration of animation in milliseconds
        animation.start()
    }
}
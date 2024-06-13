package com.cpp.unsmoke.ui.personalizedplan

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.local.preferences.dataStore
import com.cpp.unsmoke.databinding.ActivityPersonalizedBinding
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

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
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left_black)

        viewModel = ObtainViewModelFactory.obtain<PersonalizedViewModel>(this)

        viewModel.currentProgress.observe(this){
            setProgressWithAnimation(it)
        }

        val loginPreferences = LoginPreferences.getInstance(applicationContext.dataStore)
        checkTokens(loginPreferences)
    }

    private fun checkTokens(loginPreferences: LoginPreferences) {
        runBlocking {
            val accessToken = loginPreferences.getToken().first()
            val refreshToken = loginPreferences.getRefreshToken().first()
            Log.d("CheckToken", "Access Token: $accessToken")
            Log.d("CheckToken", "Refresh Token: $refreshToken")
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
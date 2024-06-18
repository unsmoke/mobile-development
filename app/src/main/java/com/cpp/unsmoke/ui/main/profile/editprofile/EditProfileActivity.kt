package com.cpp.unsmoke.ui.main.profile.editprofile

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityEditProfileBinding
import com.cpp.unsmoke.ui.main.profile.ProfileViewModel
import com.cpp.unsmoke.utils.helper.ui.drawableToFile
import com.cpp.unsmoke.utils.helper.ui.uriToFile
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    private var currentImageUri: Uri? = null
    private var file: File? = null
    private var fileName: String = "not found"
    private var originalUsername: String? = null
    private var originalProfileUrl: String? = null

    private lateinit var launcherGallery: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.editProfileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left)

        val viewModel = ObtainViewModelFactory.obtainAuth<EditProfileViewModel>(this)

        viewModel.getUserData().observe(this) { result ->
            when (result) {
                is com.cpp.unsmoke.data.remote.Result.Loading -> {
                    // Show loading indicator if necessary
                }
                is com.cpp.unsmoke.data.remote.Result.Success -> {
                    val userData = result.data
                    originalUsername = userData.data?.username
                    originalProfileUrl = userData.data?.profileUrl
                    // Update UI with userData
                    binding.edtUsername.setText(originalUsername)
                    if (originalProfileUrl != null) {
                        binding.ivItemPhoto.load(originalProfileUrl)
                    } else {
                        binding.ivItemPhoto.load(R.drawable.photo_profile)
                    }

                }
                is com.cpp.unsmoke.data.remote.Result.Error -> {
                    // Handle error
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnNext.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val isUsernameUpdated = username != originalUsername
            val isImageUpdated = currentImageUri != null

            if (!isUsernameUpdated && !isImageUpdated) {
                Toast.makeText(this, "You did not change any data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalUsername = if (isUsernameUpdated) username else originalUsername
            val imageFile = if (isImageUpdated) {
                file
            } else {
                val drawable = getDrawable(R.drawable.photo_profile)
                drawable?.let {
                    drawableToFile(this, it, "profile_image.png")
                }
            }

            if (finalUsername.isNullOrEmpty()) {
                binding.edtUsername.error = "Username cannot be empty"
                return@setOnClickListener
            }

            viewModel.updateUserData(finalUsername, imageFile).observe(this) { result ->
                when (result) {
                    is com.cpp.unsmoke.data.remote.Result.Loading -> {
                        // Show loading indicator if necessary
                    }
                    is com.cpp.unsmoke.data.remote.Result.Success -> {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is com.cpp.unsmoke.data.remote.Result.Error -> {
                        // Handle error
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        launcherGallery = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                currentImageUri.let {
                    if (it != null) {
                        file = uriToFile(it, this)
                    }
                }

                showImage()
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

        binding.tvEditPhoto.setOnClickListener {
            startGallery()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivItemPhoto.setImageURI(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

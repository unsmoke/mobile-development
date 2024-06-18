package com.cpp.unsmoke.ui.main.profile.editprofile

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityEditProfileBinding
import com.cpp.unsmoke.utils.helper.ui.uriToFile
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    private var currentImageUri: Uri? = null
    private var file: File? = null
    private var fileName: String = "not found"

    private lateinit var launcherGallery: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.editProfileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left)

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
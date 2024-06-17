package com.cpp.unsmoke.ui.main.profile.editprofile

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.editProfileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.fi_ss_arrow_small_left)

        binding.edtPhotoProfile.setOnClickListener {
            startGallery()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            currentImageUri.let {
                if (it != null) {
                    file = uriToFile(it, this)
                    fileName = file?.name ?: "not found"
                    binding.edtPhotoProfile.text = Editable.Factory.getInstance().newEditable(fileName)
                }
            }
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
}
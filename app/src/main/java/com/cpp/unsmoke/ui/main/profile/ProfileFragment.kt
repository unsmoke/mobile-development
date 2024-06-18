package com.cpp.unsmoke.ui.main.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.lifecycle.ViewModelProvider
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentHomeBinding
import com.cpp.unsmoke.databinding.FragmentPlanBinding
import com.cpp.unsmoke.databinding.FragmentProfileBinding
import com.cpp.unsmoke.ui.auth.login.LoginActivity
import com.cpp.unsmoke.ui.main.home.HomeViewModel
import com.cpp.unsmoke.ui.main.profile.editprofile.EditProfileActivity
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileViewModel = ObtainViewModelFactory.obtainAuth<ProfileViewModel>(requireActivity())

        val alertBuilder = AlertDialog.Builder(requireActivity())

        binding.btnEdit.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            // Call logout function from ProfileViewModel
            alertBuilder.setTitle(getString(R.string.logout))
            alertBuilder.setMessage("Are You Sure?")
            alertBuilder.setPositiveButton("Ok") { _, _ ->
                profileViewModel.logout()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finishAffinity()
            }.create().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.cpp.unsmoke.ui.personalizedplan.screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedElevenBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedTenBinding
import com.cpp.unsmoke.ui.auth.login.LoginActivity
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel


class PersonalizedElevenFragment : Fragment() {

    private var _binding: FragmentPersonalizedElevenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedElevenBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ViewModelProvider(requireActivity())[PersonalizedViewModel::class.java]

        personalizedViewModel.currentProgress.observe(viewLifecycleOwner) { progress ->
            if (progress > 100) {
                hideProgressBar()
            } else {
                showProgressBar()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showProgressBar()
                personalizedViewModel.decreaseProgress()
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })

        binding.btnSubmitPlan.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun hideProgressBar() {
        // Mendapatkan tampilan progress bar dari tata letak induk dan menyembunyikannya
        val progressBar = requireActivity().findViewById<View>(R.id.progress_bar)
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        // Mendapatkan tampilan progress bar dari tata letak induk dan menampilkannya
        val progressBar = requireActivity().findViewById<View>(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
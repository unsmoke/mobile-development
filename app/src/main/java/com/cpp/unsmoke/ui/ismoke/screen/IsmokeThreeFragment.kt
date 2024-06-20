package com.cpp.unsmoke.ui.ismoke.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentIsmokeThreeBinding
import com.cpp.unsmoke.ui.ismoke.IsmokeViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class IsmokeThreeFragment : Fragment() {
    private var _binding: FragmentIsmokeThreeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIsmokeThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ismokeViewModel = ObtainViewModelFactory.obtainAuth<IsmokeViewModel>(requireActivity())

        hideToolBar()

        binding.tvSmokeConsume.text = ismokeViewModel.getUserCigConsumed().toString()

        ismokeViewModel.getUserData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.cpp.unsmoke.data.remote.Result.Success -> {
                    binding.tvSmokeQuota.text = result.data.data?.cigarettesQuota?.get(0).toString()
                }
                is com.cpp.unsmoke.data.remote.Result.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
                is com.cpp.unsmoke.data.remote.Result.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnNext.setOnClickListener {
            requireActivity().finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                requireActivity().finish()
            }
        })
    }

    private fun hideToolBar() {
        // Mendapatkan tampilan progress bar dari tata letak induk dan menyembunyikannya
        val toolBar = requireActivity().findViewById<View>(R.id.ismoke_toolbar)
        toolBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedFiveBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedSixBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel


class PersonalizedSixFragment : Fragment() {

    private var _binding: FragmentPersonalizedSixBinding? = null
    private val binding get() = _binding!!

    private var isUsingECigaretteSet = false
    private var isUsingECigaretteValue = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedSixBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ViewModelProvider(requireActivity())[PersonalizedViewModel::class.java]

        personalizedViewModel.isUsingECigarette.observe(viewLifecycleOwner) { isUsingECigarette ->
            when (isUsingECigarette) {
                1 -> binding.rgECigarettes.check(R.id.rb_yes_with_nicotine)
                2 -> binding.rgECigarettes.check(R.id.rb_yes_no_nicotine)
                3 -> binding.rgECigarettes.check(R.id.rb_no_but_used)
                4 -> binding.rgECigarettes.check(R.id.rb_never_used)
            }
        }

        binding.rgECigarettes.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_yes_with_nicotine -> {
                    isUsingECigaretteSet = true
                    isUsingECigaretteValue = 1
                }
                R.id.rb_yes_no_nicotine -> {
                    isUsingECigaretteSet = true
                    isUsingECigaretteValue = 2
                }
                R.id.rb_no_but_used -> {
                    isUsingECigaretteSet = true
                    isUsingECigaretteValue = 3
                }
                R.id.rb_never_used -> {
                    isUsingECigaretteSet = true
                    isUsingECigaretteValue = 4
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (isUsingECigaretteSet) {
                personalizedViewModel.setIsUsingECigarette(isUsingECigaretteValue)
                personalizedViewModel.increaseProgress()
                Navigation.findNavController(view).navigate(R.id.action_personalizedSixFragment_to_personalizedSevenFragment)
            } else {
                showToast("Please fill in this field")
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                personalizedViewModel.decreaseProgress()
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
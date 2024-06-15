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
import com.cpp.unsmoke.databinding.FragmentPersonalizedSevenBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedSixBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory


class PersonalizedSevenFragment : Fragment() {

    private var _binding: FragmentPersonalizedSevenBinding? = null
    private val binding get() = _binding!!

    private var isUsingOtherTobaccoSet = false
    private var isUsingOtherTobaccoValue = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedSevenBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ObtainViewModelFactory.obtainAuth<PersonalizedViewModel>(requireActivity())

        personalizedViewModel.isUsingOtherTobacco.observe(viewLifecycleOwner) { isUsingOtherTobacco ->
            when (isUsingOtherTobacco) {
                1 -> binding.rgTobaccoProducts.check(R.id.rb_yes_use_every_day)
                2 -> binding.rgTobaccoProducts.check(R.id.rb_yes_use_sometimes)
                3 -> binding.rgTobaccoProducts.check(R.id.rb_no_used_to)
                4 -> binding.rgTobaccoProducts.check(R.id.rb_no_never_used)
            }
        }

        binding.rgTobaccoProducts.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_yes_use_every_day -> {
                    isUsingOtherTobaccoSet = true
                    isUsingOtherTobaccoValue = 1
                }
                R.id.rb_yes_use_sometimes -> {
                    isUsingOtherTobaccoSet = true
                    isUsingOtherTobaccoValue = 2
                }
                R.id.rb_no_used_to -> {
                    isUsingOtherTobaccoSet = true
                    isUsingOtherTobaccoValue = 3
                }
                R.id.rb_no_never_used -> {
                    isUsingOtherTobaccoSet = true
                    isUsingOtherTobaccoValue = 4
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (isUsingOtherTobaccoSet) {
                personalizedViewModel.setIsUsingOtherTobacco(isUsingOtherTobaccoValue)
                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedSevenFragment_to_personalizedEightFragment ).onClick(it)
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
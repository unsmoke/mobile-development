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
import com.cpp.unsmoke.databinding.FragmentPersonalizedTwoBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory


class PersonalizedFiveFragment : Fragment() {
    private var _binding: FragmentPersonalizedFiveBinding? = null
    private val binding get() = _binding!!

    private var isNicotineMedSet = false
    private var isNicotineMedValue = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedFiveBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ObtainViewModelFactory.obtainAuth<PersonalizedViewModel>(requireActivity())

        personalizedViewModel.isNicotineMed.observe(viewLifecycleOwner) { isNicotineMed ->
            if (isNicotineMed) {
                binding.rgNicotineAlternativeMedicines.check(R.id.rb_yes_fragment_five)
            } else {
                binding.rgNicotineAlternativeMedicines.check(R.id.rb_no_fragment_five)
            }
        }

        binding.rgNicotineAlternativeMedicines.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_yes_fragment_five -> {
                    isNicotineMedSet = true
                    isNicotineMedValue = true
                }
                R.id.rb_no_fragment_five -> {
                    isNicotineMedSet = true
                    isNicotineMedValue = false
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (isNicotineMedSet) {
                personalizedViewModel.setIsNicotineMed(isNicotineMedValue)
                personalizedViewModel.increaseProgress()
                Navigation.findNavController(view).navigate(R.id.action_personalizedFiveFragment_to_personalizedSixFragment)
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
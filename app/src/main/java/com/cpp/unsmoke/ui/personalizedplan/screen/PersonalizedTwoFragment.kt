package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedOneBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedTwoBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class PersonalizedTwoFragment : Fragment() {
    private var _binding: FragmentPersonalizedTwoBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedGender: String

    private var isGenderSet = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedTwoBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ObtainViewModelFactory.obtain<PersonalizedViewModel>(requireActivity())

        binding.radioButtonGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedGender = when (checkedId) {
                R.id.radioButtonMan -> "Male"
                R.id.radioButtonWoman -> "Female"
                else -> ""
            }
            isGenderSet = true
        }

        personalizedViewModel.gender.observe(viewLifecycleOwner) { gender ->
            when (gender) {
                "Male" -> binding.radioButtonGroup.check(R.id.radioButtonMan)
                "Female" -> binding.radioButtonGroup.check(R.id.radioButtonWoman)
                else -> {
                    binding.radioButtonGroup.clearCheck()
                    binding.btnNext.isEnabled = false
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (isGenderSet){
                personalizedViewModel.setGender(selectedGender)
                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedTwoFragment_to_personalizedThreeFragment ).onClick(it)
            } else {
                Toast.makeText(requireContext(), "Please select your gender", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
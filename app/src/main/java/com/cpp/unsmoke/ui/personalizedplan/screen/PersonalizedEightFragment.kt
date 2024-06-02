package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedEightBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedSixBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.DateFormat
import java.util.Locale

class PersonalizedEightFragment : Fragment() {

    private var _binding: FragmentPersonalizedEightBinding? = null
    private val binding get() = _binding!!
    private var currentPicker: MaterialTimePicker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedEightBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ViewModelProvider(requireActivity())[PersonalizedViewModel::class.java]

        binding.edtWakeUpTime.setOnClickListener {
            openTimePicker("Wake Up Time") { hour, minute ->
                binding.edtWakeUpTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
            }
        }

        binding.edtFirstSmokeTime.setOnClickListener {
            openTimePicker("First Smoke Time") { hour, minute ->
                binding.edtFirstSmokeTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
            }
        }

        binding.btnNext.setOnClickListener {
            personalizedViewModel.increaseProgress()
            Navigation.createNavigateOnClickListener(R.id.action_personalizedEightFragment_to_personalizedNineFragment ).onClick(it)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                personalizedViewModel.decreaseProgress()
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })
    }

    private fun openTimePicker(title: String, onTimeSelected: (Int, Int) -> Unit) {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        // Dismiss any existing picker before showing a new one
        currentPicker?.dismiss()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setTitleText(title)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            onTimeSelected(timePicker.hour, timePicker.minute)
            currentPicker = null // Reset the current picker reference
        }

        timePicker.addOnCancelListener {
            currentPicker = null // Reset the current picker reference
        }

        timePicker.addOnDismissListener {
            currentPicker = null // Reset the current picker reference
        }

        currentPicker = timePicker
        timePicker.show(parentFragmentManager, title)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
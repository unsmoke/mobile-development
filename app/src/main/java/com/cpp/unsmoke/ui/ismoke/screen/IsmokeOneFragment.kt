package com.cpp.unsmoke.ui.ismoke.screen

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentIsmokeOneBinding
import com.cpp.unsmoke.ui.ismoke.IsmokeViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Locale


class IsmokeOneFragment : Fragment() {
    private var _binding: FragmentIsmokeOneBinding? = null
    private val binding get() = _binding!!

    private var currentPicker: MaterialTimePicker? = null

    private var isSmokeTimeSet = false
    private var isCigaretteCountSet = false
    private var isFeelingsSet = false

    private var smokeTime = ""
    private var cigaretteCount = ""
    private var feelings = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIsmokeOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ismokeViewModel = ObtainViewModelFactory.obtainAuth<IsmokeViewModel>(requireActivity())

        ismokeViewModel.smokeTime.observe(viewLifecycleOwner) { smokeTime ->
            binding.edtSmokingTime.setText(smokeTime)
        }

        ismokeViewModel.cigaretteCount.observe(viewLifecycleOwner) { cigaretteCount ->
            binding.edtSmokingQuantity.setText(cigaretteCount)
        }

        ismokeViewModel.feelings.observe(viewLifecycleOwner) { feelings ->
            binding.edtSmokingFeelings.setText(feelings)
        }

        binding.edtSmokingTime.setOnClickListener {
            openTimePicker("Smoking Time") { hour, minute ->
                binding.edtSmokingTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
                smokeTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                isSmokeTimeSet = true
            }
        }

        binding.edtSmokingQuantity.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty() && text.toString().toInt() > 0) {
                try {
                    cigaretteCount = text.toString()
                    isCigaretteCountSet = true
                    binding.textInputLayoutSmokingQuantity.isErrorEnabled = false
                } catch (e: NumberFormatException) {
                    binding.textInputLayoutSmokingQuantity.error = "Invalid number format"
                    isCigaretteCountSet = false
                }
            } else {
                isCigaretteCountSet = false
                binding.textInputLayoutSmokingQuantity.error = "Please fill in this field\nMinimum value is 1"
            }
        }

        binding.edtSmokingFeelings.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                feelings = text.toString()
                isFeelingsSet = true
                binding.textInputLayoutSmokingFeelings.isErrorEnabled = false
            } else {
                isFeelingsSet = false
                binding.textInputLayoutSmokingFeelings.error = "Please fill in this field"
            }
        }

        binding.btnNext.setOnClickListener {
            Log.d("IsmokeOneFragment", "Smoke time set: $isSmokeTimeSet, Cigarette count set: $isCigaretteCountSet, Feelings set: $isFeelingsSet, Validate times: ${validateTimes()}")
            if (isSmokeTimeSet && isCigaretteCountSet && isFeelingsSet && validateTimes()) {
                ismokeViewModel.setSmokeTime(smokeTime)
                ismokeViewModel.setCigaretteCount(cigaretteCount)
                ismokeViewModel.setFeelings(feelings)
                Navigation.findNavController(view)
                    .navigate(R.id.action_ismokeOneFragment_to_ismokeTwoFragment)
            } else {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            })
    }

    private fun openTimePicker(title: String, onTimeSelected: (Int, Int) -> Unit) {
        val isSystem24Hour = DateFormat.is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        // Dismiss any existing picker before showing a new one
        currentPicker?.dismiss()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setTheme(R.style.ThemeOverlay_App_TimePicker)
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

    private fun validateTimes(): Boolean {
        val smokeTime = binding.edtSmokingTime.text.toString()

        if (smokeTime.isEmpty()) {
            binding.textInputLayoutSmokingTime.error = "Please set wake up time"
            isSmokeTimeSet = false
            return false
        } else {
            binding.textInputLayoutSmokingTime.error = null
            binding.textInputLayoutSmokingTime.isErrorEnabled = false
            isSmokeTimeSet = true
            return true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
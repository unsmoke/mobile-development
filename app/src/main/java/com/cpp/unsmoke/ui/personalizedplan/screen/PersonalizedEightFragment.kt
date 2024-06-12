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
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

class PersonalizedEightFragment : Fragment() {

    private var _binding: FragmentPersonalizedEightBinding? = null
    private val binding get() = _binding!!
    private var currentPicker: MaterialTimePicker? = null

    private var valueInMinutes = 0

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

        val personalizedViewModel = ObtainViewModelFactory.obtain<PersonalizedViewModel>(requireActivity())

        personalizedViewModel.wakeUpTime.observe(viewLifecycleOwner) { wakeUpTime ->
            binding.edtWakeUpTime.setText(wakeUpTime)
        }

        personalizedViewModel.firstSmoke.observe(viewLifecycleOwner) { firstSmokeTime ->
            binding.edtFirstSmokeTime.setText(firstSmokeTime)
        }

        binding.edtWakeUpTime.setOnClickListener {
            openTimePicker("Wake Up Time") { hour, minute ->
                personalizedViewModel.setWakeUpTime(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
                binding.edtWakeUpTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
            }
        }

        binding.edtFirstSmokeTime.setOnClickListener {
            openTimePicker("First Smoke Time") { hour, minute ->
                personalizedViewModel.setFirstSmoke(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
                binding.edtFirstSmokeTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
            }
        }

        binding.btnNext.setOnClickListener {
            if (validateTimes()) {
                personalizedViewModel.setSmokingStartTime(valueInMinutes)
                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedEightFragment_to_personalizedNineFragment).onClick(it)
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

    private fun openTimePicker(title: String, onTimeSelected: (Int, Int) -> Unit) {
        val isSystem24Hour = is24HourFormat(requireContext())
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
        val wakeUpTime = binding.edtWakeUpTime.text.toString()
        val firstSmokeTime = binding.edtFirstSmokeTime.text.toString()

        if (wakeUpTime.isNotEmpty() && firstSmokeTime.isNotEmpty()) {
            val wakeUpParts = wakeUpTime.split(":")
            val firstSmokeParts = firstSmokeTime.split(":")

            val wakeUpHour = wakeUpParts[0].toInt()
            val wakeUpMinute = wakeUpParts[1].toInt()
            val firstSmokeHour = firstSmokeParts[0].toInt()
            val firstSmokeMinute = firstSmokeParts[1].toInt()

            val wakeUpCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, wakeUpHour)
                set(Calendar.MINUTE, wakeUpMinute)
            }

            val firstSmokeCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, firstSmokeHour)
                set(Calendar.MINUTE, firstSmokeMinute)
            }

            if (firstSmokeCalendar.before(wakeUpCalendar)) {
                binding.textInputLayoutFirstSmokeTime.error = "First smoke time cannot be before wake up time"
                return false
            } else {
                binding.textInputLayoutFirstSmokeTime.error = null
            }

            val diffInMillis = firstSmokeCalendar.timeInMillis - wakeUpCalendar.timeInMillis
            val diffInMinutes = (diffInMillis / (1000 * 60)).toInt()

            valueInMinutes = diffInMinutes

            return true
        } else {
            if (wakeUpTime.isEmpty()) {
                binding.textInputLayoutWakeUpTime.error = "Please set wake up time"
            } else {
                binding.textInputLayoutWakeUpTime.error = null
            }

            if (firstSmokeTime.isEmpty()) {
                binding.textInputLayoutFirstSmokeTime.error = "Please set first smoke time"
            } else {
                binding.textInputLayoutFirstSmokeTime.error = null
            }

            return false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
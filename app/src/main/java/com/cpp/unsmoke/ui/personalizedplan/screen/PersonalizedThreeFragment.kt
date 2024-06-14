package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import android.util.Log
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
import com.cpp.unsmoke.databinding.FragmentPersonalizedThreeBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.ui.DateFormatter
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale

class PersonalizedThreeFragment : Fragment() {
    private var _binding: FragmentPersonalizedThreeBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var rawDate: String

    private var isDateSet = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPersonalizedThreeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ObtainViewModelFactory.obtainAuth<PersonalizedViewModel>(requireActivity())

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        binding.textInputLayoutSmokingStartDate.isErrorEnabled = false

        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Length of time you have been smoking")
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setSelection(today)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .build()
            )
            .build()

        datePicker.addOnPositiveButtonClickListener {
            datePicker.selection?.let { selection ->
                dateFormat(selection)
                rawDate = selection.toString()
                isDateSet = true
            }
        }

        personalizedViewModel.firstSmokeDate.observe(viewLifecycleOwner) {date ->
            if (date.isNotEmpty()){
                rawDate = date
                binding.textInputLayoutSmokingStartDate.isErrorEnabled = false
                dateFormat(date.toLong())
                isDateSet = true
            } else {
                binding.textInputLayoutSmokingStartDate.error = "Please Select Your Date of First Smoke from view model"
                binding.btnNext.isEnabled = false
            }
        }

        binding.edtSmokingStartDate.setOnClickListener {
            if (!datePicker.isAdded) {
                datePicker.show(parentFragmentManager, "date_picker")
            }
        }

        binding.btnNext.setOnClickListener{
            if (isDateSet){
                personalizedViewModel.setFirstSmokeDate(rawDate)
                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedThreeFragment_to_personalizedFourFragment ).onClick(it)
            } else {
                binding.textInputLayoutSmokingStartDate.error = "Please Select Your Date of First Smoke from button"
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

    private fun dateFormat(date: Long) {
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
        binding.edtSmokingStartDate.setText(format)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
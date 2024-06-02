package com.cpp.unsmoke.ui.personalizedplan.screen

import android.app.DatePickerDialog
import android.os.Bundle
import android.provider.CalendarContract.Calendars
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedOneBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PersonalizedOneFragment : Fragment() {
    private var _binding: FragmentPersonalizedOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedOneBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ViewModelProvider(requireActivity())[PersonalizedViewModel::class.java]

        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Your Date of Birth")
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            datePicker.selection?.let { it1 ->
                dateFormat(it1)
                Log.d("FORMAT TANGGAL", it1.toString())
            }
        }

        binding.edtDateOfBirth.setOnClickListener {
            if (!datePicker.isAdded) {
                datePicker.show(parentFragmentManager, "date_picker")
            }
        }

        binding.btnNext.setOnClickListener{
            personalizedViewModel.increaseProgress()
            Navigation.createNavigateOnClickListener(R.id.action_personalizedOneFragment_to_personalizedTwoFragment).onClick(it)
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
        binding.edtDateOfBirth.setText(format)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
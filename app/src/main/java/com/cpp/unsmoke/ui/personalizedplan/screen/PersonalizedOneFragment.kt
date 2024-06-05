package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedOneBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale

class PersonalizedOneFragment : Fragment() {
    private var _binding: FragmentPersonalizedOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePicker: MaterialDatePicker<Long>

    private lateinit var provinceList: MutableList<String>
    private lateinit var provinceCityMap: MutableMap<String, MutableList<String>>
    private lateinit var provinceCodeMap: MutableMap<Int, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ViewModelProvider(requireActivity())[PersonalizedViewModel::class.java]

        personalizedViewModel.dateOfBirth.observe(viewLifecycleOwner) { date ->
            // Menetapkan nilai dateOfBirth ke input layout
            binding.edtDateOfBirth.setText(date)
        }

        personalizedViewModel.province.observe(viewLifecycleOwner) { province ->
            if (province.isNotEmpty()){
                binding.textInputLayoutCity.visibility = View.VISIBLE
                binding.tvCity.visibility = View.VISIBLE

                // Menetapkan nilai province ke input layout
                binding.autoCompleteProvince.setText(province)
                setupCityDropdown(province) // Memperbarui dropdown kota berdasarkan provinsi yang dipilih
            }
        }

        personalizedViewModel.city.observe(viewLifecycleOwner) { city ->
            // Menetapkan nilai city ke input layout
            binding.autoCompleteCity.setText(city)
        }

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Your Date of Birth")
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setSelection(today)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .build()
            )
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

        binding.btnNext.setOnClickListener {
            personalizedViewModel.setDateOfBirth(binding.edtDateOfBirth.text.toString())
            personalizedViewModel.setProvince(binding.autoCompleteProvince.text.toString())
            personalizedViewModel.setCity(binding.autoCompleteCity.text.toString())

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

        loadCSVData()
        setupProvinceDropdown()
    }

    private fun dateFormat(date: Long) {
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
        binding.edtDateOfBirth.setText(format)
    }

    private fun loadCSVData() {
        provinceList = mutableListOf()
        provinceCityMap = mutableMapOf()
        provinceCodeMap = mutableMapOf()

        try {
            val inputStream = requireContext().assets.open("provinces.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val values = line?.split(",")
                val provinceCode = values?.get(0)?.toIntOrNull()
                val provinceName = values?.get(1)
                if (provinceCode != null && provinceName != null) {
                    provinceList.add(provinceName)
                    provinceCodeMap[provinceCode] = provinceName
                }
            }
            reader.close()

            val inputStreamRegencies = requireContext().assets.open("regencies.csv")
            val readerRegencies = BufferedReader(InputStreamReader(inputStreamRegencies))
            while (readerRegencies.readLine().also { line = it } != null) {
                val values = line?.split(",")
                val provinceCode = values?.get(1)?.toIntOrNull()
                val cityName = values?.get(2)
                if (provinceCode != null && cityName != null) {
                    val provinceName = provinceCodeMap[provinceCode]
                    if (provinceName != null) {
                        provinceCityMap.getOrPut(provinceName) { mutableListOf() }.add(cityName)
                    }
                }
            }
            readerRegencies.close()

            Log.d("CSV_LOAD", "Loaded provinces: $provinceList")
            Log.d("CSV_LOAD", "Loaded province-city map: $provinceCityMap")

        } catch (e: Exception) {
            Log.e("CSV_LOAD_ERROR", "Error loading CSV files", e)
        }
    }

    private fun setupProvinceDropdown() {
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, provinceList)
        (binding.autoCompleteProvince as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.autoCompleteProvince.setOnItemClickListener { parent, _, position, _ ->
            val selectedProvince = parent.getItemAtPosition(position) as String
            if (selectedProvince.isNotEmpty()) {
                binding.textInputLayoutCity.visibility = View.VISIBLE
                binding.tvCity.visibility = View.VISIBLE
                setupCityDropdown(selectedProvince)
            }
        }
    }

    private fun setupCityDropdown(province: String? = null) {
        val cities = province?.let { provinceCityMap[it] } ?: emptyList()
        Log.d("SETUP_CITY_DROPDOWN", "Selected province: $province, Cities: $cities")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cities)
        (binding.autoCompleteCity as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

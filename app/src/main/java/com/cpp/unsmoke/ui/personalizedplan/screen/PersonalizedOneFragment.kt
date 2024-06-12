package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.remote.responses.personalized.DataItemCity
import com.cpp.unsmoke.data.remote.responses.personalized.DataItemProvince
import com.cpp.unsmoke.databinding.FragmentPersonalizedOneBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale

class PersonalizedOneFragment : Fragment() {
    private var _binding: FragmentPersonalizedOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePicker: MaterialDatePicker<Long>

    private var isDateOfBirthSet = false
    private var isProvinceSet = false
    private var isCitySet = false

    private lateinit var rawDate: String

    private var provinces: List<DataItemProvince?>? = null

    private var provinceId: Int = 0
    private var cityId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ObtainViewModelFactory.obtain<PersonalizedViewModel>(requireActivity())

        personalizedViewModel.provincesLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {

                is com.cpp.unsmoke.data.remote.Result.Error -> Toast.makeText(requireContext(), "Failed to load provinces: ${result.error}", Toast.LENGTH_LONG).show()
                com.cpp.unsmoke.data.remote.Result.Loading -> Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                is com.cpp.unsmoke.data.remote.Result.Success -> setupProvinceDropdown(result.data.data)
            }
        }

        binding.autoCompleteProvince.setOnItemClickListener { parent, _, position, _ ->
            val selectedProvinceName = parent.getItemAtPosition(position) as String

            // Find the DataItemProvince based on the selected name
            val selectedProvince = provinces?.find { it?.provinceName == selectedProvinceName }

            selectedProvince?.provinceId?.let { personalizedViewModel.loadCities(it) }

            provinceId = selectedProvince?.provinceId ?: 0

            if (selectedProvince != null){
                binding.textInputLayoutCity.visibility = View.VISIBLE
                binding.tvCity.visibility = View.VISIBLE
                isProvinceSet = true
            }
        }

        personalizedViewModel.citiesLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.cpp.unsmoke.data.remote.Result.Error -> Toast.makeText(requireContext(), "Failed to load cities: ${result.error}", Toast.LENGTH_LONG).show()
                com.cpp.unsmoke.data.remote.Result.Loading -> Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                is com.cpp.unsmoke.data.remote.Result.Success -> {
                    setupCityDropdown(result.data.data)
                    personalizedViewModel.setCities(result.data.data)
                }
            }
        }

        personalizedViewModel.loadProvinces()

        
        personalizedViewModel.dateOfBirth.observe(viewLifecycleOwner) { date ->
            // Menetapkan nilai dateOfBirth ke input layout
            dateFormat(date.toLong())
            updateButtonState()
        }

        personalizedViewModel.province.observe(viewLifecycleOwner) { province ->
            if (province != 0){
                binding.textInputLayoutCity.visibility = View.VISIBLE
                binding.tvCity.visibility = View.VISIBLE

                val selectedProvince = provinces?.find { it?.provinceId == province }

                selectedProvince?.provinceId?.let { personalizedViewModel.loadCities(it) }

                binding.autoCompleteProvince.setText(selectedProvince?.provinceName)
            }
            updateButtonState()
        }

        personalizedViewModel.city.observe(viewLifecycleOwner) { city ->
            cityId = city
        }

        personalizedViewModel.cities.observe(viewLifecycleOwner) { city ->
            // Menetapkan nilai city ke input layout
            val selectedCity = city?.find { it?.cityId == cityId }

            binding.autoCompleteCity.setText(selectedCity?.cityName)
            updateButtonState()
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
                rawDate = it1.toString()
                dateFormat(it1)
                Log.d("FORMAT TANGGAL", it1.toString())
                isDateOfBirthSet = true
            }
        }

        binding.edtDateOfBirth.setOnClickListener {
            if (!datePicker.isAdded) {
                datePicker.show(parentFragmentManager, "date_picker")
            }
        }

        binding.btnNext.setOnClickListener {
            if (isDateOfBirthSet && isProvinceSet && isCitySet){
                personalizedViewModel.setDateOfBirth(rawDate)
                personalizedViewModel.setProvince(provinceId)
                personalizedViewModel.setCity(cityId)
                Log.d("PROVINCE_ID", provinceId.toString())
                Log.d("CITY_ID", cityId.toString())

                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedOneFragment_to_personalizedTwoFragment).onClick(it)
            } else {
                Toast.makeText(requireContext(), "Please fill out all input", Toast.LENGTH_SHORT).show()
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

    private fun updateButtonState() {
        // Memperbarui keadaan tombol berdasarkan apakah semua nilai telah diatur atau tidak
        binding.btnNext.isEnabled = isDateOfBirthSet && isProvinceSet && isCitySet
    }

    private fun dateFormat(date: Long) {
        try {
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
            binding.edtDateOfBirth.setText(format)
        } catch (e: Exception){
            e.printStackTrace()
            showToast("Error formatting date")
        }

    }

    private fun setupProvinceDropdown(provinces: List<DataItemProvince?>?) {
        this.provinces = provinces // Store the list
        val adapter = provinces?.let {
            ArrayAdapter(requireContext(), R.layout.dropdown_item, it.map { it?.provinceName })
        }
        binding.autoCompleteProvince.setAdapter(adapter)
    }

    private fun setupCityDropdown(cities: List<DataItemCity?>?) {
        val adapter = cities?.let { ArrayAdapter(requireContext(), R.layout.dropdown_item, it.map { it?.cityName }) }
        binding.autoCompleteCity.setAdapter(adapter)

        binding.autoCompleteCity.setOnItemClickListener { parent, _, position, _ ->
            val selectedCityName = parent.getItemAtPosition(position) as String

            // Find the DataItemCity based on the selected name
            val selectedCity = cities?.find { it?.cityName == selectedCityName }

            cityId = selectedCity?.cityId ?: 0

            if (selectedCity != null){
                isCitySet = true
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
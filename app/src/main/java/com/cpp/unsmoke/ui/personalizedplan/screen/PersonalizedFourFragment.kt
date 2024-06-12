package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedFiveBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedFourBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedOneBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import kotlin.properties.Delegates

class PersonalizedFourFragment : Fragment() {
    private var _binding: FragmentPersonalizedFourBinding? = null
    private val binding get() = _binding!!

    private var isCigarettesPerDaySet = false
    private var isCigarettesPerPackSet = false
    private var isPackPriceSet = false

    private var cigarettesPerDayValue: Int = 0
    private var cigarettesPerPackValue: Int = 0
    private var packPriceValue: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedFourBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textInputLayoutTotalCigarette.isErrorEnabled = false
        binding.textInputLayoutTotalCigarettePerpack.isErrorEnabled = false
        binding.textInputLayoutPricePerpack.isErrorEnabled = false

        binding.edtTotalCigarettePerpack.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                try {
                    cigarettesPerPackValue = text.toString().toInt()
                    isCigarettesPerPackSet = true
                    binding.textInputLayoutTotalCigarettePerpack.isErrorEnabled = false
                } catch (e: NumberFormatException) {
                    binding.textInputLayoutTotalCigarettePerpack.error = "Invalid number format"
                    isCigarettesPerPackSet = false
                }
            } else {
                binding.textInputLayoutTotalCigarettePerpack.error = "Please fill in this field"
            }
        }

        binding.edtTotalCigarette.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                try {
                    cigarettesPerDayValue = text.toString().toInt()
                    isCigarettesPerDaySet = true
                    binding.textInputLayoutTotalCigarette.isErrorEnabled = false
                } catch (e: NumberFormatException) {
                    binding.textInputLayoutTotalCigarette.error = "Invalid number format"
                    isCigarettesPerDaySet = false
                }
            } else {
                binding.textInputLayoutTotalCigarette.error = "Please fill in this field"
            }
        }

        binding.edtPricePerpack.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                try {
                    // Remove commas before parsing the string to a float
                    val cleanPriceString = text.toString().replace(",", "")
                    packPriceValue = cleanPriceString.toFloat()
                    isPackPriceSet = true
                    binding.textInputLayoutPricePerpack.isErrorEnabled = false
                } catch (e: NumberFormatException) {
                    binding.textInputLayoutPricePerpack.error = "Invalid price format"
                    isPackPriceSet = false
                }
            } else {
                binding.textInputLayoutPricePerpack.error = "Please fill in this field"
            }
        }



        val personalizedViewModel = ObtainViewModelFactory.obtain<PersonalizedViewModel>(requireActivity())

        personalizedViewModel.cigarettesPerDay.observe(viewLifecycleOwner) { cigarettesPerDay ->
            if (cigarettesPerDay != 0) {
                binding.edtTotalCigarette.setText(cigarettesPerDay.toString())
                cigarettesPerDayValue = cigarettesPerDay
                isCigarettesPerDaySet = true
                binding.textInputLayoutTotalCigarette.isErrorEnabled = false
            } else {
                binding.textInputLayoutTotalCigarette.error = "Please fill in this field"
                binding.edtTotalCigarette.setText("")
            }
        }

        personalizedViewModel.cigarettesPerPack.observe(viewLifecycleOwner) { cigarettesPerPack ->
            if (cigarettesPerPack != 0) {
                binding.edtTotalCigarettePerpack.setText(cigarettesPerPack.toString())
                cigarettesPerPackValue = cigarettesPerPack
                isCigarettesPerPackSet = true
                binding.textInputLayoutTotalCigarettePerpack.isErrorEnabled = false
            } else {
                binding.textInputLayoutTotalCigarettePerpack.error = "Please fill in this field"
                binding.edtTotalCigarettePerpack.setText("")
            }
        }

        personalizedViewModel.packPrice.observe(viewLifecycleOwner) { packPrice ->
            if (packPrice != 0f) {
                binding.edtPricePerpack.setText(packPrice.toString())
                packPriceValue = packPrice
                isPackPriceSet = true
                binding.textInputLayoutPricePerpack.isErrorEnabled = false
            } else {
                binding.textInputLayoutPricePerpack.error = "Please fill in this field"
                binding.edtPricePerpack.setText("")
            }
        }

        binding.btnNext.setOnClickListener {
            if (isCigarettesPerDaySet && isCigarettesPerPackSet && isPackPriceSet) {
                personalizedViewModel.setCigarettesPerDay(cigarettesPerDayValue)
                personalizedViewModel.setCigarettesPerPack(cigarettesPerPackValue)
                personalizedViewModel.setPackPrice(packPriceValue)
                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedFourFragment_to_personalizedFiveFragment ).onClick(it)
            } else {
                if (!isCigarettesPerDaySet) {
                    binding.textInputLayoutTotalCigarette.error = "Please fill in this field"
                }
                if (!isCigarettesPerPackSet) {
                    binding.textInputLayoutTotalCigarettePerpack.error = "Please fill in this field"
                }
                if (!isPackPriceSet) {
                    binding.textInputLayoutPricePerpack.error = "Please fill in this field"
                }
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
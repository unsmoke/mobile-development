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
import com.cpp.unsmoke.databinding.FragmentPersonalizedEightBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedNineBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class PersonalizedNineFragment : Fragment() {

    private var _binding: FragmentPersonalizedNineBinding? = null
    private val binding get() = _binding!!

    private var isDepressionSet = false
    private var isDepressionValue = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedNineBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ObtainViewModelFactory.obtain<PersonalizedViewModel>(requireActivity())

        personalizedViewModel.isDepressed.observe(viewLifecycleOwner) { isDepressed ->
            if (isDepressed) {
                binding.rgFragmentNine.check(R.id.rb_yes_fragment_nine)
            } else {
                binding.rgFragmentNine.check(R.id.rb_no_fragment_nine)
            }
        }

        binding.rgFragmentNine.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_yes_fragment_nine -> {
                    isDepressionSet = true
                    isDepressionValue = true
                }
                R.id.rb_no_fragment_nine -> {
                    isDepressionSet = true
                    isDepressionValue = false
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (isDepressionSet){
                personalizedViewModel.setIsDepressed(isDepressionValue)
                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedNineFragment_to_personalizedTenFragment ).onClick(it)
            } else {
                showToast("Please select an option")
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

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
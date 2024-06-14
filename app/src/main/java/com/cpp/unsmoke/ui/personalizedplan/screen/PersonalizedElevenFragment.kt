package com.cpp.unsmoke.ui.personalizedplan.screen

import android.content.Intent
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
import com.cpp.unsmoke.databinding.FragmentPersonalizedElevenBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedTenBinding
import com.cpp.unsmoke.ui.auth.login.LoginActivity
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory


class PersonalizedElevenFragment : Fragment() {

    private var _binding: FragmentPersonalizedElevenBinding? = null
    private val binding get() = _binding!!

    private var isLast7DaysSet = false
    private var isLast7DaysValue = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedElevenBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ObtainViewModelFactory.obtainAuth<PersonalizedViewModel>(requireActivity())

        personalizedViewModel.isLast7Days.observe(viewLifecycleOwner) { isLast7Days ->
            if (isLast7Days) {
                binding.rgFragmentEleven.check(R.id.rb_yes_fragment_eleven)
            } else {
                binding.rgFragmentEleven.check(R.id.rb_no_fragment_eleven)
            }
        }

        binding.rgFragmentEleven.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_yes_fragment_eleven -> {
                    isLast7DaysSet = true
                    isLast7DaysValue = true
                }
                R.id.rb_no_fragment_eleven -> {
                    isLast7DaysSet = true
                    isLast7DaysValue = false
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if(isLast7DaysSet){
                personalizedViewModel.setIsLast7Days(isLast7DaysValue)
                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedElevenFragment_to_personalizedTwelveFragment ).onClick(it)
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
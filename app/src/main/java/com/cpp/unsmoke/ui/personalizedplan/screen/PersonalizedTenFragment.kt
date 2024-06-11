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
import com.cpp.unsmoke.databinding.FragmentPersonalizedNineBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedTenBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel


class PersonalizedTenFragment : Fragment() {

    private var _binding: FragmentPersonalizedTenBinding? = null
    private val binding get() = _binding!!

    private var isDemoralizedSet = false
    private var isDemoralizedValue = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedTenBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ViewModelProvider(requireActivity())[PersonalizedViewModel::class.java]

        personalizedViewModel.isSpirit.observe(viewLifecycleOwner) { isSpirit ->
            if (isSpirit) {
                binding.rgFragmentTen.check(R.id.rb_yes_fragment_ten)
            } else {
                binding.rgFragmentTen.check(R.id.rb_no_fragment_ten)
            }
        }

        binding.rgFragmentTen.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_yes_fragment_ten -> {
                    isDemoralizedSet = true
                    isDemoralizedValue = true
                }
                R.id.rb_no_fragment_ten -> {
                    isDemoralizedSet = true
                    isDemoralizedValue = false
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if(isDemoralizedSet){
                personalizedViewModel.setIsSpirit(isDemoralizedValue)
                personalizedViewModel.getAllData()
                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedTenFragment_to_personalizedElevenFragment ).onClick(it)
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
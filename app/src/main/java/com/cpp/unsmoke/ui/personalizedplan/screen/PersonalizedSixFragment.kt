package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedFiveBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedSixBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel


class PersonalizedSixFragment : Fragment() {

    private var _binding: FragmentPersonalizedSixBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedSixBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ViewModelProvider(requireActivity())[PersonalizedViewModel::class.java]

        binding.btnNext.setOnClickListener {
            personalizedViewModel.increaseProgress()
            Navigation.createNavigateOnClickListener(R.id.action_personalizedSixFragment_to_personalizedSevenFragment ).onClick(it)
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
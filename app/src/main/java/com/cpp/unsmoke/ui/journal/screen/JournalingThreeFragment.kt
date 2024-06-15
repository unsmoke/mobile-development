package com.cpp.unsmoke.ui.journal.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentJournalingThreeBinding
import com.cpp.unsmoke.ui.journal.JournalViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class JournalingThreeFragment : Fragment() {
    private var _binding: FragmentJournalingThreeBinding? = null
    private val binding get() = _binding!!

    private var isChallengeSet = false
    private var isChallengeValue = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJournalingThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val journalingViewModel = ObtainViewModelFactory.obtainAuth<JournalViewModel>(requireActivity())

        journalingViewModel.challenge.observe(viewLifecycleOwner) { challenge ->
            binding.edtChallenges.setText(challenge)
        }

        binding.textInputLayoutChallenges.isErrorEnabled = false

        binding.edtChallenges.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty()) {
                isChallengeSet = true
                isChallengeValue = text.toString()
                binding.textInputLayoutChallenges.isErrorEnabled = false
            } else {
                isChallengeSet = false
                binding.textInputLayoutChallenges.error = "Please fill in this field"
            }
        }

        binding.btnNext.setOnClickListener {
            if (isChallengeSet) {
                journalingViewModel.setChallenge(isChallengeValue)
                Navigation.findNavController(view).navigate(R.id.action_journalingThreeFragment_to_journalingFourFragment)
            } else {
                binding.textInputLayoutChallenges.error = "Please fill in this field"
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
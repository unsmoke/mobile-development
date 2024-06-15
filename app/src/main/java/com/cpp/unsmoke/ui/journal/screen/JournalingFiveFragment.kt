package com.cpp.unsmoke.ui.journal.screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentJournalingFiveBinding
import com.cpp.unsmoke.ui.journal.JournalViewModel
import com.cpp.unsmoke.ui.main.MainActivity
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class JournalingFiveFragment : Fragment() {
    private var _binding: FragmentJournalingFiveBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJournalingFiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val journalingViewModel = ObtainViewModelFactory.obtainAuth<JournalViewModel>(requireActivity())

        journalingViewModel.feeling.observe(viewLifecycleOwner) { feeling ->
            binding.tvFeeling.text = getString(R.string.summary_emotions, feeling)
        }

        journalingViewModel.challenge.observe(viewLifecycleOwner) { challenge ->
            binding.tvChallenge.text =
                getString(R.string.summary_challenges, challenge)
        }

        journalingViewModel.commitment.observe(viewLifecycleOwner) { commitment ->
            binding.tvCommitment.text =
                getString(R.string.summary_commitment, commitment)
        }

        binding.btnNext.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })

        requireActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
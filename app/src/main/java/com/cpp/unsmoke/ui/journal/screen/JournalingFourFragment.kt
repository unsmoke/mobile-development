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
import com.cpp.unsmoke.databinding.FragmentJournalingFourBinding
import com.cpp.unsmoke.ui.journal.JournalViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class JournalingFourFragment : Fragment() {
    private var _binding: FragmentJournalingFourBinding? = null
    private val binding get() = _binding!!

    private var isCommitmentSet = false
    private var isCommitmentValue = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJournalingFourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val journalingViewModel = ObtainViewModelFactory.obtainAuth<JournalViewModel>(requireActivity())

        journalingViewModel.commitment.observe(viewLifecycleOwner) { commitment ->
            binding.edtCommitment.setText(commitment)
        }

        binding.textInputLayoutCommitment.isErrorEnabled = false

        binding.edtCommitment.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                isCommitmentSet = true
                isCommitmentValue = text.toString()
                binding.textInputLayoutCommitment.isErrorEnabled = false
            } else {
                isCommitmentSet = false
                binding.textInputLayoutCommitment.error = "Please fill in this field"
            }
        }

        binding.btnNext.setOnClickListener {
            if (isCommitmentSet) {
                journalingViewModel.setCommitment(isCommitmentValue)
                Navigation.findNavController(view).navigate(R.id.action_journalingFourFragment_to_journalingFiveFragment)
            } else {
                binding.textInputLayoutCommitment.error = "Please fill in this field"
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
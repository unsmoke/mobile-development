package com.cpp.unsmoke.ui.journal.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentJournalingTwoBinding
import com.cpp.unsmoke.ui.journal.JournalViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class JournalingTwoFragment : Fragment() {
    private var _binding: FragmentJournalingTwoBinding? = null
    private val binding get() = _binding!!

    private var isFeelingSet = false
    private var isFeelingValue = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJournalingTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val journalingViewModel = ObtainViewModelFactory.obtainAuth<JournalViewModel>(requireActivity())

        binding.rgJournalingOne.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_angry_fragment_two -> {
                    isFeelingSet = true
                    isFeelingValue = "angry"
                }
                R.id.rb_happy_fragment_two -> {
                    isFeelingSet = true
                    isFeelingValue = "happy"
                }
                R.id.rb_fear_fragment_two -> {
                    isFeelingSet = true
                    isFeelingValue = "fear"
                }
                R.id.rb_sad_fragment_two -> {
                    isFeelingSet = true
                    isFeelingValue = "sad"
                }
                R.id.rb_calm_fragment_two -> {
                    isFeelingSet = true
                    isFeelingValue = "calm"
                }
            }
        }

        journalingViewModel.feeling.observe(viewLifecycleOwner) { feeling ->
            when (feeling) {
                "angry" -> binding.rgJournalingOne.check(R.id.rb_angry_fragment_two)
                "happy" -> binding.rgJournalingOne.check(R.id.rb_happy_fragment_two)
                "fear" -> binding.rgJournalingOne.check(R.id.rb_fear_fragment_two)
                "sad" -> binding.rgJournalingOne.check(R.id.rb_sad_fragment_two)
                "calm" -> binding.rgJournalingOne.check(R.id.rb_calm_fragment_two)
            }
        }

        binding.btnNext.setOnClickListener {
            if (isFeelingSet) {
                journalingViewModel.setFeeling(isFeelingValue)
                Navigation.findNavController(view).navigate(R.id.action_journalingTwoFragment_to_journalingThreeFragment)
            } else {
                Toast.makeText(requireContext(), "Please select one of the options", Toast.LENGTH_SHORT).show()
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
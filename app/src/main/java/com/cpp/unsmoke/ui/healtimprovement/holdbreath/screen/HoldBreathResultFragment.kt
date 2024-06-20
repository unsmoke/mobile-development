package com.cpp.unsmoke.ui.healtimprovement.holdbreath.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentHoldBreathBinding
import com.cpp.unsmoke.databinding.FragmentHoldBreathResultBinding
import com.cpp.unsmoke.ui.healtimprovement.holdbreath.HoldBreathViewModel
import com.cpp.unsmoke.utils.helper.gamification.Gamification
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory


class HoldBreathResultFragment : Fragment() {
    private var _binding: FragmentHoldBreathResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHoldBreathResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ObtainViewModelFactory.obtainAuth<HoldBreathViewModel>(requireActivity())

        viewModel.duration.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Duration: $it", Toast.LENGTH_SHORT).show()
            binding.tvDurationTime.text = it.toString()
        }

        viewModel.highestRecord.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Highest Record: $it", Toast.LENGTH_SHORT).show()
            binding.tvHighestRecord.text = it.toString()
        }

        binding.tvXpEarnedPoint.text = Gamification.BREATH_REWARD.toString()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                requireActivity().finish()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
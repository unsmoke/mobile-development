package com.cpp.unsmoke.ui.ismoke.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentIsmokeTwoBinding
import com.cpp.unsmoke.databinding.FragmentJournalingOneBinding
import com.cpp.unsmoke.ui.ismoke.IsmokeViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class IsmokeTwoFragment : Fragment() {
    private var _binding: FragmentIsmokeTwoBinding? = null
    private val binding get() = _binding!!

    private var isReasonSet = false
    private var reason = ""

    private var totalSmoke: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIsmokeTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ismokeViewModel = ObtainViewModelFactory.obtainAuth<IsmokeViewModel>(requireActivity())

        ismokeViewModel.smokingReason.observe(viewLifecycleOwner) { reason ->
            when (reason) {
                binding.rbReason1.text.toString() -> binding.rbReason1.isChecked = true
                binding.rbReason2.text.toString() -> binding.rbReason2.isChecked = true
                binding.rbReason3.text.toString() -> binding.rbReason3.isChecked = true
                binding.rbReason4.text.toString() -> binding.rbReason4.isChecked = true
                binding.rbReason5.text.toString() -> binding.rbReason5.isChecked = true
                binding.rbReason6.text.toString() -> binding.rbReason6.isChecked = true
                binding.rbReason7.text.toString() -> binding.rbReason7.isChecked = true
                binding.rbReason8.text.toString() -> binding.rbReason8.isChecked = true
                binding.rbReason9.text.toString() -> binding.rbReason9.isChecked = true
            }
        }

        binding.rgIsmokeTwo.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_reason_1 -> {
                    isReasonSet = true
                    reason = binding.rbReason1.text.toString()
                }
                R.id.rb_reason_2 -> {
                    isReasonSet = true
                    reason = binding.rbReason2.text.toString()
                }
                R.id.rb_reason_3 -> {
                    isReasonSet = true
                    reason = binding.rbReason3.text.toString()
                }
                R.id.rb_reason_4 -> {
                    isReasonSet = true
                    reason = binding.rbReason4.text.toString()
                }
                R.id.rb_reason_5 -> {
                    isReasonSet = true
                    reason = binding.rbReason5.text.toString()
                }
                R.id.rb_reason_6 -> {
                    isReasonSet = true
                    reason = binding.rbReason6.text.toString()
                }
                R.id.rb_reason_7 -> {
                    isReasonSet = true
                    reason = binding.rbReason7.text.toString()
                }
                R.id.rb_reason_8 -> {
                    isReasonSet = true
                    reason = binding.rbReason8.text.toString()
                }
                R.id.rb_reason_9 -> {
                    isReasonSet = true
                    reason = binding.rbReason9.text.toString()
                }
            }
        }

        ismokeViewModel.totalCigNow.observe(viewLifecycleOwner) { total ->
            totalSmoke = total
        }

        ismokeViewModel.getUserData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.cpp.unsmoke.data.remote.Result.Success -> {
                    binding.btnNext.setOnClickListener {
                        if (totalSmoke < (result.data.data?.cigarettesQuota?.get(0) ?: 0)) {
                            ismokeViewModel.setSmokingReason(reason)
                            Navigation.findNavController(view).navigate(R.id.action_ismokeTwoFragment_to_ismokeThreeFragment)
                        } else {
                            Navigation.findNavController(view).navigate(R.id.action_ismokeTwoFragment_to_ismokeFourFragment)
                        }
                    }
                }
                is com.cpp.unsmoke.data.remote.Result.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                is com.cpp.unsmoke.data.remote.Result.Loading -> {
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                }
            }
        }

//        binding.btnNext.setOnClickListener {
//            if (isReasonSet) {
//                ismokeViewModel.setSmokingReason(reason)
//                Navigation.findNavController(view).navigate(R.id.action_ismokeTwoFragment_to_ismokeThreeFragment)
//            } else {
//                Navigation.findNavController(view).navigate(R.id.action_ismokeTwoFragment_to_ismokeFourFragment)
//            }
//        }

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
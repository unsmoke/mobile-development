package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedElevenBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedTwelveBinding
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class PersonalizedTwelveFragment : Fragment() {
    private var _binding: FragmentPersonalizedTwelveBinding? = null
    private val binding get() = _binding!!

    private var isMotivatedSet = false
    private var isMotivatedValue = "More"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedTwelveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ObtainViewModelFactory.obtain<PersonalizedViewModel>(requireActivity())

        personalizedViewModel.motivation.observe(viewLifecycleOwner) { motivation ->
            when (motivation) {
                "Health" -> binding.rgMotivationQuitSmoking.check(R.id.rb_a)
                "Family and friends" -> binding.rgMotivationQuitSmoking.check(R.id.rb_b)
                "Finance" -> binding.rgMotivationQuitSmoking.check(R.id.rb_c)
                "Work demands" -> binding.rgMotivationQuitSmoking.check(R.id.rb_d)
                "Medical program" -> binding.rgMotivationQuitSmoking.check(R.id.rb_e)
                "Healthy lifestyle" -> binding.rgMotivationQuitSmoking.check(R.id.rb_f)
                "More" -> binding.rgMotivationQuitSmoking.check(R.id.rb_g)
            }
        }

        binding.rgMotivationQuitSmoking.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_a -> {
                    isMotivatedSet = true
                    isMotivatedValue = "Health"
                }
                R.id.rb_b -> {
                    isMotivatedSet = true
                    isMotivatedValue = "Family and friends"
                }
                R.id.rb_c -> {
                    isMotivatedSet = true
                    isMotivatedValue = "Finance"
                }
                R.id.rb_d -> {
                    isMotivatedSet = true
                    isMotivatedValue = "Work demands"
                }
                R.id.rb_e -> {
                    isMotivatedSet = true
                    isMotivatedValue = "Medical program"
                }
                R.id.rb_f -> {
                    isMotivatedSet = true
                    isMotivatedValue = "Healthy lifestyle"
                }
                R.id.rb_g -> {
                    isMotivatedSet = true
                    isMotivatedValue = "More"
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (isMotivatedSet) {
                personalizedViewModel.setMotivation(isMotivatedValue)
                personalizedViewModel.increaseProgress()
                Navigation.createNavigateOnClickListener(R.id.action_personalizedTwelveFragment_to_personalizedThirteenFragment ).onClick(it)
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
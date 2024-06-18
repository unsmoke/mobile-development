package com.cpp.unsmoke.ui.main.plan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import coil.decode.SvgDecoder
import coil.load
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentHomeBinding
import com.cpp.unsmoke.databinding.FragmentPlanBinding
import com.cpp.unsmoke.ui.main.home.HomeViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class PlanFragment : Fragment() {
    private var _binding: FragmentPlanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: PlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ObtainViewModelFactory.obtainAuth<PlanViewModel>(requireActivity())

        viewModel.loadLungUrl()

        viewModel.currentLungUrl.observe(viewLifecycleOwner) {
            binding.icLungPlan.load(it){
                decoderFactory { result, options, _ ->
                    SvgDecoder(result.source, options)
                }
            }
        }

        viewModel.getUserData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.cpp.unsmoke.data.remote.Result.Loading -> {
                    // Show loading indicator if necessary
                }
                is com.cpp.unsmoke.data.remote.Result.Success -> {
                    val userData = result.data
                    // Update UI with userData
                    binding.tvCoinUser.text = userData.data?.balanceCoin.toString()
                    binding.tvXpUser.text = userData.data?.exp.toString()
                    binding.tvStreakUser.text = "${userData.data?.streakCount.toString()} Streak"
                }
                is com.cpp.unsmoke.data.remote.Result.Error -> {
                    // Handle error
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        playImageViewAnimation()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun playImageViewAnimation() {
        val scaleX = ObjectAnimator.ofFloat(binding.icLungPlan, View.SCALE_X, 1f, 1.2f, 1f).apply {
            duration = 3200
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val scaleY = ObjectAnimator.ofFloat(binding.icLungPlan, View.SCALE_Y, 1f, 1.2f, 1f).apply {
            duration = 3200
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
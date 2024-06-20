package com.cpp.unsmoke.ui.healtimprovement.holdbreath.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.databinding.FragmentHoldBreathBinding
import com.cpp.unsmoke.ui.healtimprovement.holdbreath.HoldBreathViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import java.util.Locale

class HoldBreathFragment : Fragment() {
    private var _binding: FragmentHoldBreathBinding? = null
    private val binding get() = _binding!!
    private var isStop: Boolean = false
    private var handlerAnimation = Handler(Looper.getMainLooper())
    private var handlerStopwatch = Handler(Looper.getMainLooper())
    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    private lateinit var viewModel: HoldBreathViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHoldBreathBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ObtainViewModelFactory.obtainAuth<HoldBreathViewModel>(requireActivity())

        binding.btnHoldBreath.setOnClickListener {
            if (!isStop){
                startStopwatch()
                startAnimation()
                setBtnToStop()
            } else {
                stopStopwatch(it)
                stopAnimation()
                setBtnToStart()
            }
        }
    }

    private fun stopAnimation() {
        isStop = true
        handlerAnimation.removeCallbacks(runnable)
    }

    private fun setBtnToStart() {
        isStop = false
        binding.btnHoldBreath.text = "Start"
    }

    private fun startAnimation() {
        isStop = false
        handlerAnimation.post(runnable)
    }

    private fun setBtnToStop() {
        isStop = true
        binding.btnHoldBreath.text = "Stop"
    }

    private var runnable = object : Runnable {
        override fun run() {
            if (isStop) {
                binding.animationOne.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                    .withEndAction {
                        binding.animationOne.apply {
                            scaleX = 1f
                            scaleY = 1f
                            alpha = 1f
                        }
                        // Post the runnable again to continue the animation loop
                        if (isStop) {
                            handlerAnimation.postDelayed(this, 999)
                        }
                    }
            }
        }

    }

    private fun startStopwatch() {
        startTime = System.currentTimeMillis()
        handlerStopwatch.post(stopwatchRunnable)
    }

    private fun stopStopwatch(view: View) {
        elapsedTime = System.currentTimeMillis() - startTime
        handlerStopwatch.removeCallbacks(stopwatchRunnable)
        val elapsedSeconds = (elapsedTime / 1000).toInt()
        viewModel.sendBreathData(elapsedSeconds)
        viewModel.sendBreathDataToRepository(elapsedSeconds).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result){
                    is Result.Success -> {
                        viewModel.setBreathIsFilled()
                        result.data.data?.highestDuration?.let { viewModel.setHighestRecord(it) }
                        Navigation.findNavController(view).navigate(R.id.action_holdBreathFragment_to_holdBreathResultFragment)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Failed to send breath data", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private val stopwatchRunnable = object : Runnable {
        override fun run() {
            val currentTime = System.currentTimeMillis()
            val elapsedMillis = currentTime - startTime
            val seconds = (elapsedMillis / 1000) % 60
            val minutes = (elapsedMillis / (1000 * 60)) % 60
            val hours = (elapsedMillis / (1000 * 60 * 60)) % 24
            binding.tvStopwatch.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
            handlerStopwatch.postDelayed(this, 1000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.cpp.unsmoke.ui.main.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import coil.decode.SvgDecoder
import coil.load
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentHomeBinding
import com.cpp.unsmoke.ui.healtimprovement.holdbreath.HoldBreathActivity
import com.cpp.unsmoke.ui.healtimprovement.holdbreath.screen.HoldBreathFragment
import com.cpp.unsmoke.ui.journal.JournalActivity
import com.cpp.unsmoke.ui.shop.ShopActivity
import com.cpp.unsmoke.utils.helper.gamification.Gamification
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ObtainViewModelFactory.obtainAuth<HomeViewModel>(requireActivity())

        viewModel.loadLungUrl()

        viewModel.currentLungUrl.observe(viewLifecycleOwner) {
            binding.icLung.load(it){
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
                    binding.tvSmokeQuota.text = userData.data?.cigarettesQuota?.getOrNull(0).toString()
                    binding.currentDayProgram.text = userData.data?.currentDay.toString()
                    binding.moneySaved.text = Gamification.formatNumberGamification(userData.data?.moneySaved ?: 0.0)
                    binding.smokeAvoided.text = userData.data?.cigarettesAvoided.toString()
                    binding.icLung.load(userData.data?.currentLung){
                        decoderFactory { result, options, _ ->
                            SvgDecoder(result.source, options)
                        }
                    }
                    viewModel.setLungUrlToLcal(userData.data?.currentLung.toString())
                }
                is com.cpp.unsmoke.data.remote.Result.Error -> {
                    // Handle error
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvSmokeConsume.text = (if (viewModel.getUserCigConsumed().toString().isEmpty()){
            "0"
        } else {
            viewModel.getUserCigConsumed()
        }).toString()

        binding.tvExp1.text = "${Gamification.RELAPSE_REWARD} XP"
        binding.tvExp2.text = "${Gamification.BREATH_REWARD} XP"

        binding.shopBtn.setOnClickListener {
            val intent = Intent(requireContext(), ShopActivity::class.java)
            startActivity(intent)
        }

        binding.cvChallenge2.setOnClickListener {
            val intent = Intent(requireActivity(), HoldBreathActivity::class.java)
            startActivity(intent)
        }

        binding.cvDailyJournal.setOnClickListener {
            val intent = Intent(requireActivity(), JournalActivity::class.java)
            startActivity(intent)
        }

        playImageViewAnimation()
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadLungUrl()

        viewModel.currentLungUrl.observe(viewLifecycleOwner) {
            binding.icLung.load(it){
                decoderFactory { result, options, _ ->
                    SvgDecoder(result.source, options)
                }
            }
        }
    }

    private fun playImageViewAnimation() {
        val scaleX = ObjectAnimator.ofFloat(binding.icLung, View.SCALE_X, 1f, 1.2f, 1f).apply {
            duration = 3200
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val scaleY = ObjectAnimator.ofFloat(binding.icLung, View.SCALE_Y, 1f, 1.2f, 1f).apply {
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
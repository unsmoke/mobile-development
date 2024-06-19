package com.cpp.unsmoke.ui.main.rank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.decode.SvgDecoder
import coil.load
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentHomeBinding
import com.cpp.unsmoke.databinding.FragmentPlanBinding
import com.cpp.unsmoke.databinding.FragmentRankBinding
import com.cpp.unsmoke.ui.main.home.HomeViewModel
import com.cpp.unsmoke.ui.main.rank.adapter.LeaderboardAdapter
import com.cpp.unsmoke.ui.main.rank.adapter.LoadingStateAdapter
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory

class RankFragment : Fragment() {
    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!
    private lateinit var rankViewModel: RankViewModel
    private lateinit var adapter: LeaderboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rankViewModel = ObtainViewModelFactory.obtainAuth<RankViewModel>(requireActivity())

        rankViewModel.getUserData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.cpp.unsmoke.data.remote.Result.Loading -> {
                    // Show loading indicator if necessary
                }
                is com.cpp.unsmoke.data.remote.Result.Success -> {
                    val userData = result.data
                    // Update UI with userData
                    binding.tvRankUser.text = userData.data?.rank.toString()
                    binding.tvPointXp.text = userData.data?.exp.toString()
                    binding.tvUsername.text = userData.data?.username
                    if (userData.data?.profileUrl != null){
                        binding.ivItemPhoto.load(userData.data.profileUrl)
                    } else {
                        binding.ivItemPhoto.load(userData.data?.currentLung){
                            decoderFactory { result, options, _ ->
                                SvgDecoder(result.source, options)
                            }
                        }
                    }

                }
                is com.cpp.unsmoke.data.remote.Result.Error -> {
                    // Handle error
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        adapter = LeaderboardAdapter()

        binding.rvRank.layoutManager = LinearLayoutManager(requireContext())

        binding.rvRank.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        rankViewModel.refreshData()
        observeRankData()
    }

    private fun observeRankData() {
        rankViewModel.rank.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
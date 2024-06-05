package com.cpp.unsmoke.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentHomeBinding
import com.cpp.unsmoke.ui.healtimprovement.holdbreath.HoldBreathActivity
import com.cpp.unsmoke.ui.healtimprovement.holdbreath.screen.HoldBreathFragment
import com.cpp.unsmoke.ui.shop.ShopActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shopBtn.setOnClickListener {
            val intent = Intent(requireContext(), ShopActivity::class.java)
            startActivity(intent)
        }

        binding.cvChallenge2.setOnClickListener {
            val intent = Intent(requireActivity(), HoldBreathActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
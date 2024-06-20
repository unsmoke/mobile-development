package com.cpp.unsmoke.ui.journal.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentJournalingResultBinding
import com.cpp.unsmoke.utils.helper.gamification.Gamification

class JournalingResultFragment : Fragment() {
    private var _binding: FragmentJournalingResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJournalingResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvXpPoint.text = Gamification.JOURNAL_REWARD.toString()

        binding.btnNext.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
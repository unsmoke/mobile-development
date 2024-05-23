package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedOneBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedThreeBinding

class PersonalizedThreeFragment : Fragment() {
    private var _binding: FragmentPersonalizedThreeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPersonalizedThreeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_personalizedThreeFragment_to_personalizedFourFragment )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
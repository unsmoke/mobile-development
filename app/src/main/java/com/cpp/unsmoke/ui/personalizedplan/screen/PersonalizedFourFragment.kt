package com.cpp.unsmoke.ui.personalizedplan.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentPersonalizedFourBinding
import com.cpp.unsmoke.databinding.FragmentPersonalizedOneBinding

class PersonalizedFourFragment : Fragment() {
    private var _binding: FragmentPersonalizedFourBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPersonalizedFourBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
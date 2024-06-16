package com.cpp.unsmoke.ui.ismoke.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.FragmentIsmokeFourBinding

class IsmokeFourFragment : Fragment() {
    private var _binding: FragmentIsmokeFourBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIsmokeFourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideToolBar()

        binding.btnNext.setOnClickListener {
            requireActivity().finish()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                isEnabled = false
                requireActivity().finish()
            }
        })
    }

    private fun hideToolBar() {
        // Mendapatkan tampilan progress bar dari tata letak induk dan menyembunyikannya
        val toolBar = requireActivity().findViewById<View>(R.id.ismoke_toolbar)
        toolBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
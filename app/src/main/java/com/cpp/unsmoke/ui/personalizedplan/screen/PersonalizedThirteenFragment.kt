package com.cpp.unsmoke.ui.personalizedplan.screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.userplan.DataItemPlan
import com.cpp.unsmoke.databinding.FragmentPersonalizedThirteenBinding
import com.cpp.unsmoke.ui.auth.login.LoginActivity
import com.cpp.unsmoke.ui.main.MainActivity
import com.cpp.unsmoke.ui.personalizedplan.PersonalizedViewModel
import com.cpp.unsmoke.ui.personalizedplan.adapter.PersonalizedAdapter
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory


class PersonalizedThirteenFragment : Fragment() {
    private var _binding: FragmentPersonalizedThirteenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalizedThirteenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personalizedViewModel = ObtainViewModelFactory.obtainAuth<PersonalizedViewModel>(requireActivity())

        binding.rvPlan.layoutManager = LinearLayoutManager(requireContext())

        setUpRecyclerView(personalizedViewModel)

        personalizedViewModel.setPersonalizedPlan().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when(result) {
                    is Result.Success -> {
                        personalizedViewModel.getUserPlan().observe(viewLifecycleOwner) { resultPlan ->
                            if (resultPlan != null) {
                                when(resultPlan) {
                                    is Result.Success -> {
                                        val plans = resultPlan.data.data
                                        Log.d("PersonalizedFragment", "Plans received: $plans")
                                        val lastThreePlans = plans?.takeLast(3)
                                        setUserData(lastThreePlans)
                                    }
                                    is Result.Error -> {
                                        if (resultPlan.error.contains("not authorized")){
                                            Toast.makeText(requireContext(), "Your are not allowed to access this resource", Toast.LENGTH_SHORT).show()
                                            personalizedViewModel.logout()
                                            toLogin()
                                        }
                                        Toast.makeText(requireContext(), "Failed to get personalized all plan", Toast.LENGTH_SHORT).show()
                                    }
                                    is Result.Loading -> {
                                        Toast.makeText(requireContext(), "Loading personalized plan", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(requireContext(), "resultPlan null", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    is Result.Error -> {
                        if (result.error.contains("not authorized")){
                            Toast.makeText(requireContext(), "Your are not allowed to access this resource", Toast.LENGTH_SHORT).show()
                            personalizedViewModel.logout()
                            toLogin()
                        }
                        Toast.makeText(requireContext(), "Failed to get personalized plan", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        Toast.makeText(requireContext(), "Loading personalized plan", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "result null", Toast.LENGTH_SHORT).show()
            }
        }

        personalizedViewModel.currentProgress.observe(viewLifecycleOwner) { progress ->
            if (progress > 100) {
                hideProgressBar()
            } else {
                showProgressBar()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showProgressBar()
                personalizedViewModel.decreaseProgress()
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })
    }

    private fun setUpRecyclerView(personalizedViewModel: PersonalizedViewModel) {
        val adapter = PersonalizedAdapter { plan ->
            plan.planId?.let {
                personalizedViewModel.updateUserPlan(it).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Success -> {
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                        is Result.Error -> {
                            if (result.error.contains("not authorized")){
                                Toast.makeText(requireContext(), "Your are not allowed to access this resource", Toast.LENGTH_SHORT).show()
                                personalizedViewModel.logout()
                                toLogin()
                            }
                            Toast.makeText(requireContext(), "Failed to update plan", Toast.LENGTH_SHORT).show()
                        }

                        is Result.Loading -> {
                            // Handle loading if necessary
                        }
                    }
                }
            }
        }
        binding.rvPlan.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlan.adapter = adapter
    }

    private fun setUserData(plans: List<DataItemPlan?>?) {
        val adapter = binding.rvPlan.adapter as PersonalizedAdapter
        adapter.submitList(plans)
    }


    private fun hideProgressBar() {
        // Mendapatkan tampilan progress bar dari tata letak induk dan menyembunyikannya
        val progressBar = requireActivity().findViewById<View>(R.id.progress_bar)
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        // Mendapatkan tampilan progress bar dari tata letak induk dan menampilkannya
        val progressBar = requireActivity().findViewById<View>(R.id.progress_bar)
        progressBar.visibility = View.VISIBLE
    }

    private fun toLogin(){
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
package com.cpp.unsmoke.ui.shop.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.shop.DataItem
import com.cpp.unsmoke.databinding.FragmentInventoryBinding
import com.cpp.unsmoke.ui.shop.ShopViewModel
import com.cpp.unsmoke.ui.shop.adapter.InventoryAdapter
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InventoryFragment : Fragment() {

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var inventoryAdapter: InventoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ObtainViewModelFactory.obtainAuth<ShopViewModel>(requireActivity())

        inventoryAdapter = InventoryAdapter { item ->
            showAlert(item)
        }
        binding.rvItemList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = inventoryAdapter
        }

        viewModel.getMyInventory().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    result.data.data?.let { items ->
                        inventoryAdapter.submitList(items)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Failed to load inventory items", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    // Handle loading state if necessary
                }
            }
        }

    }

    private fun showAlert(item: DataItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(item.name)
            .setMessage(item.description)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

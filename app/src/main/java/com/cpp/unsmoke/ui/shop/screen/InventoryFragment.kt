package com.cpp.unsmoke.ui.shop.screen

import android.os.Bundle
import android.util.Log
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

        inventoryAdapter = InventoryAdapter(viewModel, viewLifecycleOwner) { item ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(item.name)
                .setMessage(item.description)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    val userId = viewModel.getUserId().toString()
                    Log.d("ShopFragment", "userId: $userId")
                    item.itemId?.let {
                        viewModel.equipItem(userId, it).observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Success -> {
                                    viewModel.setLungUrlToLcal(result.data.data?.currentLung ?: "")
                                    viewModel.setLungIdToLocal(result.data.data?.itemId ?: "")
                                    Toast.makeText(requireContext(), "Item equipped", Toast.LENGTH_SHORT).show()
                                    viewModel.getMyInventory().observe(viewLifecycleOwner) { resultLung ->
                                        when (resultLung) {
                                            is Result.Success -> {
                                                Toast.makeText(requireContext(), "Item Reload", Toast.LENGTH_SHORT).show()
                                                resultLung.data.data?.let { items ->
                                                    viewModel.setLungUrl()
                                                    viewModel.setLungId()
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
                                is Result.Error -> {
                                    Toast.makeText(requireContext(), "Failed to equip item", Toast.LENGTH_SHORT).show()
                                }
                                is Result.Loading -> {
                                    // Handle loading state if necessary
                                }
                            }
                        }
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        binding.rvItemList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = inventoryAdapter
        }


        viewModel.getMyInventory().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    viewModel.setLungUrl()
                    viewModel.setLungId()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

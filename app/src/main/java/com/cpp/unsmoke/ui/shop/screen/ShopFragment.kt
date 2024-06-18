package com.cpp.unsmoke.ui.shop.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import coil.decode.SvgDecoder
import coil.load
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.local.preferences.LoginPreferences
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.shop.DataItem
import com.cpp.unsmoke.databinding.FragmentShopBinding
import com.cpp.unsmoke.ui.shop.ShopViewModel
import com.cpp.unsmoke.ui.shop.adapter.ShopAdapter
import com.cpp.unsmoke.utils.helper.viewmodel.ObtainViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var shopAdapter: ShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ObtainViewModelFactory.obtainAuth<ShopViewModel>(requireActivity())

        viewModel.setLungUrl()

        viewModel.currentLungUrl.observe(viewLifecycleOwner) {
            binding.ivLung.load(it){
                decoderFactory { result, options, _ ->
                    SvgDecoder(result.source, options)
                }
            }
        }

        binding.inventoryBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_shopFragment_to_inventoryFragment)
        }

        shopAdapter = ShopAdapter { item ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(item.name)
                .setMessage(item.description)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    val userId = viewModel.getUserId().toString()
                    Log.d("ShopFragment", "userId: $userId")
                    item.itemId?.let {
                        viewModel.buyItem(userId, it).observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Success -> {
                                    viewModel.getMyShop().observe(viewLifecycleOwner) { resultShop ->
                                        when (resultShop) {
                                            is Result.Success -> {
                                                resultShop.data.data?.let { items ->
                                                    shopAdapter.submitList(items)
                                                }
                                            }
                                            is Result.Error -> {
                                                Toast.makeText(requireContext(), "Failed to load shop items", Toast.LENGTH_SHORT).show()
                                            }
                                            is Result.Loading -> {
                                                Toast.makeText(requireContext(), "Loading shop items", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                    Toast.makeText(requireContext(), "Item purchased successfully", Toast.LENGTH_SHORT).show()
                                }

                                is Result.Error -> {
                                    Toast.makeText(requireContext(), "Failed to purchase item", Toast.LENGTH_SHORT).show()
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
            layoutManager = GridLayoutManager(context, 2)
            adapter = shopAdapter
        }

        viewModel.getMyShop().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    result.data.data?.let { items ->
                        shopAdapter.submitList(items)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Failed to load shop items", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Toast.makeText(requireContext(), "Loading shop items", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

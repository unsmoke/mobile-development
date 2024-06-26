package com.cpp.unsmoke.ui.shop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load
import com.cpp.unsmoke.data.remote.responses.shop.DataItem
import com.cpp.unsmoke.databinding.ShopItemRowBinding

class ShopAdapter(private val onItemClickCallback: (DataItem) -> Unit) :
    ListAdapter<DataItem, ShopAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(private val binding: ShopItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem, onItemClickCallback: (DataItem) -> Unit) {
            with(binding) {
                ivItem.load(item.imgUrl) {
                    decoderFactory { result, options, _ ->
                        SvgDecoder(result.source, options)
                    }
                }

                tvItemPrice.text = item.price.toString()
                root.setOnClickListener {
                    onItemClickCallback(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ShopItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, onItemClickCallback)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.itemId == newItem.itemId
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

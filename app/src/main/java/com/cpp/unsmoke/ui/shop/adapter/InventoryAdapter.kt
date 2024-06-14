package com.cpp.unsmoke.ui.shop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cpp.unsmoke.data.remote.responses.shop.DataItem
import com.cpp.unsmoke.databinding.InventoryItemBinding

class InventoryAdapter(private val onItemClickCallback: (DataItem) -> Unit) :
    ListAdapter<DataItem, InventoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(private val binding: InventoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem, onItemClickCallback: (DataItem) -> Unit) {
            with(binding) {
                Glide.with(root.context)
                    .load(item.imgUrl)
                    .into(ivItemPhoto)
                tvItemName.text = item.name
                root.setOnClickListener {
                    onItemClickCallback(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = InventoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

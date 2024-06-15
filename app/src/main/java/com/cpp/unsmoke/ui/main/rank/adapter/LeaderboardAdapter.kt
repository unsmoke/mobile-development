package com.cpp.unsmoke.ui.main.rank.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cpp.unsmoke.data.remote.responses.leaderboard.LeaderboardItem
import com.cpp.unsmoke.databinding.RankItemRowBinding

class LeaderboardAdapter :
    PagingDataAdapter<LeaderboardItem, LeaderboardAdapter.LeaderboardViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding = RankItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class LeaderboardViewHolder(private val binding: RankItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LeaderboardItem) {
            binding.tvRankNumber.text = data.rank.toString()
            binding.tvUsername.text = data.username
            binding.tvUserXp.text = "${data.exp} XP"
            // You can set the photo if there is any, here is a placeholder example:
            // binding.ivItemPhoto.setImageResource(R.drawable.ic_placeholder)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LeaderboardItem>() {
            override fun areItemsTheSame(oldItem: LeaderboardItem, newItem: LeaderboardItem): Boolean {
                return oldItem.rank == newItem.rank
            }

            override fun areContentsTheSame(oldItem: LeaderboardItem, newItem: LeaderboardItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

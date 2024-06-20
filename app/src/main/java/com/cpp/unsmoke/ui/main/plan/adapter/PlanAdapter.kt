package com.cpp.unsmoke.ui.main.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load
import com.cpp.unsmoke.R

data class Badge(val title: String, val description: String, val badgeUrl: String, val isAchieved: Boolean)

class PlanAdapter : ListAdapter<Badge, PlanAdapter.BadgeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.milestone_item_row, parent, false)
        return BadgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val badge = getItem(position)
        holder.bind(badge)
    }

    class BadgeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val badgeImageView: ImageView = view.findViewById(R.id.iv_badges)
        private val badgeTitle: TextView = view.findViewById(R.id.tv_badges_name)
        private val badgeDescription: TextView = view.findViewById(R.id.tv_badges_descriptions)

        fun bind(badge: Badge) {
            badgeTitle.text = badge.title
            badgeDescription.text = badge.description
            badgeImageView.load(badge.badgeUrl){
                decoderFactory { result, options, _ ->
                    SvgDecoder(result.source, options)
                }
            }
            badgeImageView.alpha = if (badge.isAchieved) 1.0f else 0.5f
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Badge>() {
            override fun areItemsTheSame(oldItem: Badge, newItem: Badge): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Badge, newItem: Badge): Boolean {
                return oldItem == newItem
            }
        }
    }
}

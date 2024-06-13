package com.cpp.unsmoke.ui.personalizedplan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cpp.unsmoke.R
import com.cpp.unsmoke.data.remote.responses.userplan.DataItemPlan
import com.cpp.unsmoke.databinding.CustomRadioButtonBinding

class PersonalizedAdapter(
    private val onItemClickCallback: (DataItemPlan) -> Unit
) : ListAdapter<DataItemPlan, PersonalizedAdapter.PlanViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = CustomRadioButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = getItem(position)
        holder.bind(plan, onItemClickCallback)
    }

    inner class PlanViewHolder(private val binding: CustomRadioButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: DataItemPlan, onItemClickCallback: (DataItemPlan) -> Unit) {
            binding.radioText.text = "Plan ${plan.duration} Day"
            if (plan.probability == 1.0) {
                binding.recommendedLabel.visibility = View.VISIBLE
                binding.radioText.setTextColor(binding.root.context.resources.getColor(R.color.accent_purple, null))
            } else {
                binding.recommendedLabel.visibility = View.GONE
                binding.radioText.setTextColor(binding.root.context.resources.getColor(R.color.accent_black, null))
            }
            binding.root.setOnClickListener {
                onItemClickCallback(plan)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemPlan>() {
            override fun areItemsTheSame(oldItem: DataItemPlan, newItem: DataItemPlan): Boolean {
                return oldItem.planId == newItem.planId
            }

            override fun areContentsTheSame(oldItem: DataItemPlan, newItem: DataItemPlan): Boolean {
                return oldItem == newItem
            }
        }
    }
}

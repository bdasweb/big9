package com.big9.app.adapter.reportAdapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.data.model.allReport.CommissionReportData
import com.big9.app.databinding.CommissionReportLayoutItemBinding
import com.big9.app.utils.`interface`.CallBack

class CommissionReportAdapter2(
    private val callBack: CallBack
) : ListAdapter<CommissionReportData, CommissionReportAdapter2.MyViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CommissionReportLayoutItemBinding =
            CommissionReportLayoutItemBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class MyViewHolder(val binding: CommissionReportLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommissionReportData, position: Int) {
            binding.apply {
                Log.d("TAG_item", "bind: " + item.comm)
                model = item
                executePendingBindings()
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CommissionReportData>() {
            override fun areItemsTheSame(
                oldItem: CommissionReportData,
                newItem: CommissionReportData
            ): Boolean {
                return oldItem.opname == newItem.opname // Adjust this based on your item comparison logic
            }

            override fun areContentsTheSame(
                oldItem: CommissionReportData,
                newItem: CommissionReportData
            ): Boolean {
                return oldItem == newItem // Adjust this based on your item comparison logic
            }
        }
    }
}
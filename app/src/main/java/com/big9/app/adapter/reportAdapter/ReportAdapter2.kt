package com.big9.app.adapter.reportAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.data.model.ReportModel
import com.big9.app.data.model.ReportPropertyModel
import com.big9.app.databinding.ReportLayoutItemBinding
import com.big9.app.utils.`interface`.CallBack

class ReportAdapter2(
    var reportPropertyModel: ReportPropertyModel,
    val callBack: CallBack
) : PagingDataAdapter<ReportModel, ReportAdapter2.MyViewHolder>(ReportModelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ReportLayoutItemBinding =
            ReportLayoutItemBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    inner class MyViewHolder(val binding: ReportLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReportModel, position: Int) {
            binding.apply {
                model = item
                cardView.setOnClickListener {
                    if (item.isClickAble == true) {
                       /* val json = item.toJson() // Implement toJson() method in your ReportModel
                        callBack.getValue(json)*/
                    }
                }
                propertyModel=reportPropertyModel
                executePendingBindings()
            }
        }
    }

    class ReportModelDiffCallback : DiffUtil.ItemCallback<ReportModel>() {
        override fun areItemsTheSame(oldItem: ReportModel, newItem: ReportModel): Boolean {
            return oldItem.id == newItem.id // Assuming you have an id field in your ReportModel
        }

        override fun areContentsTheSame(oldItem: ReportModel, newItem: ReportModel): Boolean {
            return oldItem == newItem
        }
    }
}
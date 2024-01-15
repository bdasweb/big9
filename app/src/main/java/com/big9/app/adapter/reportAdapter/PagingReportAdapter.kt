package com.big9.app.adapter.reportAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.data.model.ReportPropertyModel
import com.big9.app.databinding.ReportPagingLayoutItemBinding
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.table.DataEntity
import com.google.gson.Gson

class PagingReportAdapter(
    val reportPropertyModel: ReportPropertyModel,
    private val callBack: CallBack
) : PagingDataAdapter<DataEntity, PagingReportAdapter.MyViewHolder>(ReportModelDiffCallback()) {
//report_paging_layout_item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ReportPagingLayoutItemBinding =
            ReportPagingLayoutItemBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            Log.d("TAG_data", "onBindViewHolder: "+it.id)
            holder.bind(it, position) }
    }

    inner class MyViewHolder(private val binding: ReportPagingLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataEntity, position: Int) {
            binding.apply {
                model = item
                cardView.setOnClickListener {
                    if (item.isClickAble == true) {
                        val gson = Gson()
                        val json = gson.toJson(item)
                        callBack.getValue(json)
                    }
                }
                propertyModel=reportPropertyModel
                executePendingBindings()
            }
        }
    }

    private class ReportModelDiffCallback : DiffUtil.ItemCallback<DataEntity>() {
        override fun areItemsTheSame(oldItem: DataEntity, newItem: DataEntity): Boolean {
            return oldItem.id == newItem.id // Assuming id is a unique identifier
        }

        override fun areContentsTheSame(oldItem: DataEntity, newItem: DataEntity): Boolean {
            return oldItem == newItem
        }
    }
}
/*

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.ReportLayoutItemBinding
import com.big9.com.big9.app.data.model.ReportModel
import com.big9.com.big9.app.data.model.ReportPropertyModel

import com.big9.com.big9.app.utils.`interface`.CallBack
import com.google.gson.Gson


class ReportAdapter(
    val reportPropertyModel: ReportPropertyModel,
    var items: List<ReportModel>,

    val callBack: CallBack
) : RecyclerView.Adapter<ReportAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        */
/*val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_items, parent, false)
        return MyViewHolder(view)*//*

       */
/* val binding = BannerItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)*//*



        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ReportLayoutItemBinding =
            ReportLayoutItemBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)


    }




    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: ReportLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReportModel, position: Int) {
            binding.apply {

                model=item
                cardView.setOnClickListener{
                    if (item.isClickAble == true){
                        val gson = Gson()
                        val json = gson.toJson(item)
                        callBack.getValue(json)
                    }

                }
                propertyModel=reportPropertyModel
                executePendingBindings()
            }

        }
    }


}*/

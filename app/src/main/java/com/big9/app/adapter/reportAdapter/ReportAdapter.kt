package com.big9.app.adapter.reportAdapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.ReportLayoutItemBinding
import com.big9.app.data.model.ReportModel
import com.big9.app.data.model.ReportPropertyModel

import com.big9.app.utils.`interface`.CallBack


class ReportAdapter(
    var reportPropertyModel: ReportPropertyModel,
    var items: ArrayList<ReportModel>,

    val callBack: CallBack
) : RecyclerView.Adapter<ReportAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        /*val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_items, parent, false)
        return MyViewHolder(view)*/
       /* val binding = BannerItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)*/


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
                       /* val gson = Gson()
                        val json = gson.toJson(item)
                        callBack.getValue(json)*/
                        item.IDP?.let {
                            callBack.getValue(it)
                        }

                    }

                }
                propertyModel=reportPropertyModel
                executePendingBindings()
            }

        }
    }


}

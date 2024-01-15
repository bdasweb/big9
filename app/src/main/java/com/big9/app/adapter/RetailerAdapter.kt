package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.UpiLayoutBinding
import com.big9.app.data.model.ListIcon

import com.big9.app.utils.`interface`.CallBack2


class RetailerAdapter(private val items: List<ListIcon>, val circleShape: Int, val callback: CallBack2) : RecyclerView.Adapter<RetailerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {



        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: UpiLayoutBinding = UpiLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: UpiLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListIcon, position: Int) {
            ///*if (position!=items.size-1) {
                binding.imgIcon.setBackgroundResource(circleShape)
            //}*/
            item.title?.let {data->
                binding.tvTitle.text = data
                binding.llContainer.setOnClickListener{
                    callback.getValue2(data,item.slag.toString())
                }
            }
            item.image?.let {image->
                binding.imgIcon.setImageResource(image)
            }


        }
    }


}
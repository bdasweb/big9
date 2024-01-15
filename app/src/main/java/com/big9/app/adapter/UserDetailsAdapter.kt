package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.ReportDetailsLayoutBinding
import com.big9.app.data.model.UserDetails



class UserDetailsAdapter( var items: List<UserDetails>) : RecyclerView.Adapter<UserDetailsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //report_layout_item


        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ReportDetailsLayoutBinding = ReportDetailsLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: ReportDetailsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserDetails, position: Int) {

            item.property.let {
                binding.tvProperty.text = it
            }
           item.reportValue.let {
                binding.tvPropertyValue.text = it
            }

        }
    }


}
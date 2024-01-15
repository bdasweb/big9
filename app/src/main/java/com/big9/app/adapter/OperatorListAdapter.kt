package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.data.model.OperatorListModel
import com.big9.app.databinding.OperatorListLayoutBinding

import com.big9.app.utils.`interface`.CallBack4


class OperatorListAdapter(private val items: ArrayList<OperatorListModel>, val callback: CallBack4) : RecyclerView.Adapter<OperatorListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        //operator_list_layout
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: OperatorListLayoutBinding = OperatorListLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: OperatorListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OperatorListModel, position: Int) {
            ///*if (position!=items.size-1) {
               // binding.imageView7.setBackgroundResource(item?.bankLogo)
            //}*/
            item.bankName?.let {bankName->
                binding.tvBankName.text = bankName
                binding.llContainer.setOnClickListener{
                    callback.getValue4(bankName,"","","")
                }
            }


            item.bankLogo?.let {bankLogo->
                binding.imageView7.setImageResource(bankLogo)
            }


        }
    }


}
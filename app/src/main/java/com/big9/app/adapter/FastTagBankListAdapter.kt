package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.FasttagBankListLayoutBinding
import com.big9.app.data.model.FastTagBankListModel

import com.big9.app.utils.`interface`.CallBack4


class FastTagBankListAdapter(private val items: List<FastTagBankListModel>, val callback: CallBack4) : RecyclerView.Adapter<FastTagBankListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        //fasttag_bank_list_layout
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: FasttagBankListLayoutBinding = FasttagBankListLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: FasttagBankListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FastTagBankListModel, position: Int) {
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
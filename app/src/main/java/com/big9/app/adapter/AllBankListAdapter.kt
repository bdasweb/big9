package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.big9.app.data.model.AllBankListData
import com.big9.app.databinding.BankListLayoutBinding

import com.big9.app.utils.`interface`.CallBack4


class AllBankListAdapter(private val items: ArrayList<AllBankListData>, val callback: CallBack4) : RecyclerView.Adapter<AllBankListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        //bank_list_layout
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: BankListLayoutBinding = BankListLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: BankListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AllBankListData, position: Int) {
            ///*if (position!=items.size-1) {
               // binding.imageView7.setBackgroundResource(item?.bankLogo)
            //}*/
            item.bankName?.let {bankName->
                binding.tvBankName.text = bankName
                binding.llContainer.setOnClickListener{
                    callback.getValue4(bankName,binding.tvIfsc.text.toString(),item.bankId.toString(),"")
                }
            }
            item.accno?.let {bankAc->
                binding.tvAcc.text = bankAc


            }

            item.ifscCode?.let {ifsc->
                binding.tvIfsc.text = ifsc

            }

            item.logo?.let {bankLogo->
                Glide.with(binding.root.context)

                    .load(bankLogo)
                    .into(binding.imageView7)
                //binding.imageView7.setImageResource(bankLogo)
            }


        }
    }


}
package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.AccountDetailsLayoutBinding
import com.big9.app.data.model.AccountDetailsModel

import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack2


class AccountDetailsAdapter(private val items: ArrayList<AccountDetailsModel>, val callback: CallBack2) : RecyclerView.Adapter<AccountDetailsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //report_layout_item

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: AccountDetailsLayoutBinding = AccountDetailsLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: AccountDetailsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccountDetailsModel, position: Int) {
            binding.model=item
            binding.cardContainer.setOnClickListener{
                item.acc?.let {acc->
                    if (item.approvedStatue) {
                        callback.getValue2(acc,item.id)
                    }
                }

            }
            binding.executePendingBindings()
        }
    }


}
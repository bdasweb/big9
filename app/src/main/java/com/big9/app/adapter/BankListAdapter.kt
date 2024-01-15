package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.BankListLayoutBinding
import com.big9.app.data.model.BankListModel

import com.big9.app.utils.`interface`.CallBack4


class BankListAdapter(private val items: List<BankListModel>, val callback: CallBack4) : RecyclerView.Adapter<BankListAdapter.MyViewHolder>() {
    private var filteredItems: List<BankListModel> = items.toList()


    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            items.toList()
        } else {
            items.filter { item ->
                item.bankName?.contains(query, ignoreCase = true) == true ||
                        item.bankAc?.contains(query, ignoreCase = true) == true ||
                        item.ifsc?.contains(query, ignoreCase = true) == true
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: BankListLayoutBinding = BankListLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(filteredItems[position], position)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    inner class MyViewHolder(val binding: BankListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BankListModel, position: Int) {
            item.bankName?.let { bankName ->
                binding.tvBankName.text = bankName
                binding.llContainer.setOnClickListener {
                    callback.getValue4(bankName, binding.tvIfsc.text.toString(), item.bankName, "")
                }
            }
            item.bankAc?.let { bankAc ->
                binding.tvAcc.text = bankAc
            }
            item.ifsc?.let { ifsc ->
                binding.tvIfsc.text = ifsc
            }
            item.bankLogo?.let { bankLogo ->
                binding.imageView7.setImageResource(bankLogo)
            }
        }
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//
//
//        //bank_list_layout
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val binding: BankListLayoutBinding = BankListLayoutBinding.inflate(layoutInflater, parent, false)
//        return MyViewHolder(binding)
//    }
//
//
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.bind(items[position],position)
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//
//
//
//    inner class MyViewHolder(val binding: BankListLayoutBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: BankListModel, position: Int) {
//            ///*if (position!=items.size-1) {
//               // binding.imageView7.setBackgroundResource(item?.bankLogo)
//            //}*/
//            item.bankName?.let {bankName->
//                binding.tvBankName.text = bankName
//                binding.llContainer.setOnClickListener{
//                    callback.getValue4(bankName,binding.tvIfsc.text.toString(),item.bankName,"")
//                }
//            }
//            item.bankAc?.let {bankAc->
//                binding.tvAcc.text = bankAc
//
//
//            }
//
//            item.ifsc?.let {ifsc->
//                binding.tvIfsc.text = ifsc
//
//            }
//
//            item.bankLogo?.let {bankLogo->
//                binding.imageView7.setImageResource(bankLogo)
//            }
//
//
//        }
//    }
//
//    fun filter(query: String) {
//        filteredItems = if (query.isEmpty()) {
//            items.toList()
//        } else {
//            items.filter { item ->
//                item.bankName?.contains(query, ignoreCase = true) == true ||
//                        item.bankAc?.contains(query, ignoreCase = true) == true ||
//                        item.ifsc?.contains(query, ignoreCase = true) == true
//            }
//        }
//        notifyDataSetChanged()
//    }
}
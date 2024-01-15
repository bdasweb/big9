package com.big9.app.adapter

import ViewRetailerData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.RetailerListLayoutBinding

import com.big9.app.utils.`interface`.CallBack4
import com.big9.app.utils.`interface`.CallBack7


class RetailerLististAdapter(private val items: ArrayList<ViewRetailerData>, val callback: CallBack7) : RecyclerView.Adapter<RetailerLististAdapter.MyViewHolder>() {
    private var filteredItems: List<ViewRetailerData> = items.toList()


    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            items.toList()
        } else {
            items.filter { item ->
                item.name?.contains(query, ignoreCase = true) == true ||
                        item.mobileNo?.contains(query, ignoreCase = true) == true ||
                        item.bname?.contains(query, ignoreCase = true) == true
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RetailerListLayoutBinding = RetailerListLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(filteredItems[position], position)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    inner class MyViewHolder(val binding: RetailerListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ViewRetailerData, position: Int) {
            /*
                @SerializedName("ID"           ) var ID          : String? = null
    @SerializedName("name"         ) var name        : String? = null
    @SerializedName("mobileNo"     ) var mobileNo    : String? = null
    @SerializedName("curr_balance" ) var currBalance : String? = null
    @SerializedName("bname"        ) var bname       : String? = null
             */
            binding.llContainer.setOnClickListener {
                callback.getValue7(item?.ID.toString(), item?.name.toString(), item.mobileNo.toString(), item.currBalance.toString(), item.bname.toString(),"","")
            }
            item.name?.let { name ->
                binding.tvBankName.text = name

            }
            item.mobileNo?.let { mobileNo ->
                binding.tvAcc.text = mobileNo
            }

            item.currBalance?.let { currBalance ->
                binding.tvIfsc.text = currBalance
            }
            item.bname?.let { bname ->
                binding.tvBankName.text = bname
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
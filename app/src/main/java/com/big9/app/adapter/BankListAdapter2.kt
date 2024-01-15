package com.big9.app.adapter

/*import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.BankListLayoutBinding
import com.big9.app.data.model.BankListModel2*/
/*
import com.big9.app.utils.`interface`.CallBack4*/
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.BankListLayoutBinding
import com.big9.app.data.model.BankListModel2
import com.big9.app.utils.`interface`.CallBack4

class BankListAdapter2(private val callback: CallBack4) :
    RecyclerView.Adapter<BankListAdapter2.MyViewHolder>(), Filterable {

    private var items: List<BankListModel2> = emptyList()
    private var filteredItems: List<BankListModel2> = emptyList()

    fun submitList(list: List<BankListModel2>) {
        items = list
        filteredItems = list
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrBlank()) {
                    filterResults.values = items
                } else {
                    val filteredList = items.filter { item ->
                        item.bankName?.contains(constraint, ignoreCase = true) == true
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = results?.values as? List<BankListModel2> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

    inner class MyViewHolder(val binding: BankListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BankListModel2, position: Int) {
            item.bankName?.let { bankName ->
                binding.tvBankName.text = bankName
                binding.llContainer.setOnClickListener {
                    callback.getValue4(bankName, binding.tvIfsc.text.toString(), item.bankName, item.bankId.toString())
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
}
/*
class BankListAdapter2(private val items: List<BankListModel2>, val callback: CallBack4) : RecyclerView.Adapter<BankListAdapter2.MyViewHolder>() {

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
        fun bind(item: BankListModel2, position: Int) {
            ///*if (position!=items.size-1) {
               // binding.imageView7.setBackgroundResource(item?.bankLogo)
            //}*/
            item.bankName?.let {bankName->
                binding.tvBankName.text = bankName
                binding.llContainer.setOnClickListener{
                    callback.getValue4(bankName,binding.tvIfsc.text.toString(),item.bankName,item.bankId.toString())
                }
            }
            item.bankAc?.let {bankAc->
                binding.tvAcc.text = bankAc


            }

            item.ifsc?.let {ifsc->
                binding.tvIfsc.text = ifsc

            }

            item.bankLogo?.let {bankLogo->
                binding.imageView7.setImageResource(bankLogo)
            }


        }
    }


}
*/

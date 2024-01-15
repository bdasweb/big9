import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.BeneficiaryListLayoutBinding
import com.big9.app.data.model.BeneficiaryListModel
import com.big9.app.data.model.BeneficiaryListModel2

import com.big9.app.utils.`interface`.CallBack4
import com.big9.app.utils.`interface`.CallBack7
import java.util.*
import kotlin.collections.ArrayList

class BeneficiaryListAdapter2(

    private var items: List<BeneficiaryListModel2>,
    private val callback: CallBack7
) : RecyclerView.Adapter<BeneficiaryListAdapter2.MyViewHolder>(), Filterable {

    private var filteredList: List<BeneficiaryListModel2> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: BeneficiaryListLayoutBinding =
            BeneficiaryListLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(filteredList[position], position)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class MyViewHolder(val binding: BeneficiaryListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BeneficiaryListModel2, position: Int) {
            item.bankName?.let { bankName ->
                binding.tvBankName.text = bankName
                binding.llContainer.setOnClickListener {
                    callback.getValue7(item.benId.toString(), binding.tvIfsc.text.toString(), item.bankName, item.bankAc.toString(),item.beneficiaryName.toString(),"","")
                }
            }
            item.bankAc?.let { bankAc ->
                binding.tvAcc.text = bankAc
            }

            item.ifsc?.let { ifsc ->
                binding.tvIfsc.text = ifsc
            }
            item.beneficiaryName?.let { beneficiaryName ->
                binding.tvBeneficiary.text = beneficiaryName
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString().lowercase(Locale.getDefault())
                filteredList = if (charString.isEmpty()) {
                    items
                } else {
                    val filtered: MutableList<BeneficiaryListModel2> = ArrayList()
                    for (item in items) {
                        if (item.bankName?.lowercase(Locale.getDefault())?.contains(charString) == true ||
                            item.beneficiaryName?.lowercase(Locale.getDefault())
                                ?.contains(charString) == true || item.bankAc?.lowercase(Locale.getDefault())
                                ?.contains(charString) == true  || item.ifsc?.lowercase(Locale.getDefault())
                                ?.contains(charString) == true
                        ) {
                            filtered.add(item)
                          }
                    }
                    filtered
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredList = filterResults.values as List<BeneficiaryListModel2>
                notifyDataSetChanged()
            }
        }
    }
}
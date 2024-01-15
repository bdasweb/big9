package com.big9.app.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.SearchServiceLayoutBinding
import com.big9.app.data.model.ListIcon

import com.big9.app.utils.`interface`.CallBack2

import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(
    private var items: List<ListIcon>,
    private val circleShape: Int,
    private val callBack: CallBack2
) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>(), Filterable {

    var filteredList: List<ListIcon> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: SearchServiceLayoutBinding =
            SearchServiceLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(filteredList[position], position)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class MyViewHolder(val binding: SearchServiceLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListIcon, position: Int) {
            binding.imgIcon.setBackgroundResource(circleShape)
            item.title?.let { title ->
                binding.tvTitle.text = title
                binding.llContainer.setOnClickListener {
                    callBack.getValue2(title,item.slag.toString())
                }
            }
            item.image?.let { image ->
                binding.imgIcon.setImageResource(image)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString().toLowerCase(Locale.getDefault())
                filteredList = if (charString.isEmpty()) {
                    items
                } else {
                    val filtered: MutableList<ListIcon> = ArrayList()
                    for (item in items) {
                        if (item.title?.toLowerCase(Locale.getDefault())?.contains(charString) == true) {
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
                filteredList = filterResults.values as List<ListIcon>
                notifyDataSetChanged()
            }
        }
    }
}
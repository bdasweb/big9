package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.big9.app.utils.`interface`.CallBack
import android.widget.Filter
import android.widget.Filterable
import com.big9.app.data.model.onBoardindPackage.CityData
import com.big9.app.databinding.CityListBinding

class CityListAdapter(var items: List<CityData>, private val callBack: CallBack) :
    RecyclerView.Adapter<CityListAdapter.MyViewHolder>(), Filterable {

    private var filteredItems: List<CityData> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CityListBinding = CityListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(filteredItems[position],position)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    inner class MyViewHolder(val binding: CityListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CityData, position: Int) {
            //binding.tvState.text = item.stateCity
            binding.model = item
            binding.tvState.setOnClickListener {

                for(item in filteredItems){
                    item.isSelecetd=false
                }
                filteredItems[position].isSelecetd=true
                callBack.getValue(item.district.toString())

                binding.executePendingBindings()
                //Log.d("TAG_filteredItems", "bind: "+filteredItems)
                notifyDataSetChanged()
            }

            binding.executePendingBindings()
        }
    }

    // Implement filtering
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSequence = constraint.toString().trim().toLowerCase()
                filteredItems = if (charSequence.isEmpty()) {
                    items
                } else {
                    items.filter { item ->
                        item.district?.lowercase()?.contains(charSequence)?:false
                    }
                }
                val results = FilterResults()
                results.values = filteredItems
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filteredItems = results.values as List<CityData>
                notifyDataSetChanged()
            }
        }
    }

    // Update the list of items
    fun updateData(newItems: List<CityData>) {
        items = newItems
        filteredItems = newItems
        notifyDataSetChanged()
    }
}
package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import android.widget.Filter
import android.widget.Filterable
import com.big9.app.databinding.StateListBinding
import com.big9.app.data.model.onBoardindPackage.StateData
import com.big9.app.utils.`interface`.CallBack2

class StateListAdapter(var items: List<StateData>, private val callBack2: CallBack2) :
    RecyclerView.Adapter<StateListAdapter.MyViewHolder>(), Filterable {

    private var filteredItems: List<StateData> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: StateListBinding = StateListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(filteredItems[position],position)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    inner class MyViewHolder(val binding: StateListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StateData, position: Int) {
            //binding.tvState.text = item.stateCity
            binding.model = item
            binding.tvState.setOnClickListener {

                for(item in filteredItems){
                    item.isSelecetd=false
                }
                filteredItems[position].isSelecetd=true
                callBack2.getValue2(item.title.toString(),item.id.toString())

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
                        item.title?.lowercase()?.contains(charSequence)?:false
                    }
                }
                val results = FilterResults()
                results.values = filteredItems
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filteredItems = results.values as List<StateData>
                notifyDataSetChanged()
            }
        }
    }

    // Update the list of items
    fun updateData(newItems: List<StateData>) {
        items = newItems
        filteredItems = newItems
        notifyDataSetChanged()
    }
}
package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.ElectricPriceListStateListBinding
import com.big9.app.data.model.ElectricListModel

import com.big9.app.utils.`interface`.CallBack

class ElectricPriceListAdapter(private val priceList: ArrayList<ElectricListModel>, val listner: CallBack) : RecyclerView.Adapter<ElectricPriceListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ElectricPriceListStateListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(priceList[position], position)
    }

    override fun getItemCount(): Int {
        return priceList.size
    }


    inner class MyViewHolder(val binding: ElectricPriceListStateListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ElectricListModel, position: Int) {
            binding.model=item
            ///*if (position!=items.size-1) {

            //}*/
            /*item.title.let {
              //  binding.tvState.text = it
            }*/

            binding.cardHeader.setOnClickListener {

                for(item in priceList){
                    item.isSelecetd=false
                }
                priceList[position].isSelecetd=true
                item.price.let {
                    listner.getValue(it)
                }
                binding.executePendingBindings()
                notifyDataSetChanged()
            }
            binding.executePendingBindings()
        }
    }


}
package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.big9.app.data.model.BottomSheetGasBillerListModel
import com.big9.app.databinding.BottomGasBillerItemsListBinding

import com.big9.app.utils.`interface`.CallBack

class BottomGasBillerListAdapter(private val operatorList: ArrayList<BottomSheetGasBillerListModel>, val listner: CallBack) : RecyclerView.Adapter<BottomGasBillerListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        //bottom_gas_biller_items
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BottomGasBillerItemsListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(operatorList[position], position)
    }

    override fun getItemCount(): Int {
        return operatorList.size
    }


    inner class MyViewHolder(val binding: BottomGasBillerItemsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BottomSheetGasBillerListModel, position: Int) {
            binding.model=item
            ///*if (position!=items.size-1) {

            //}*/
            /*item.title.let {
              //  binding.tvState.text = it
            }*/
            item.image.let {
                Glide.with(binding.imgLogo.context)
                    .load(it)
                    .into(binding.imgLogo)
            }
            binding.clHeader.setOnClickListener{
                item.billerName.let {
                    listner.getValue(it)
                }

            }
            binding.executePendingBindings()
        }
    }


}
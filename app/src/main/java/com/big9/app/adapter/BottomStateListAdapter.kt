package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.BottomStateListBinding
import com.big9.app.data.model.BottomSheetStateListModel

import com.big9.app.utils.`interface`.CallBack

class BottomStateListAdapter(private val operatorList: ArrayList<BottomSheetStateListModel>, val listner: CallBack) : RecyclerView.Adapter<BottomStateListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BottomStateListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(operatorList[position], position)
    }

    override fun getItemCount(): Int {
        return operatorList.size
    }


    inner class MyViewHolder(val binding: BottomStateListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BottomSheetStateListModel, position: Int) {
            binding.model=item
            ///*if (position!=items.size-1) {

            //}*/
            /*item.title.let {
              //  binding.tvState.text = it
            }*/

            binding.clHeader.setOnClickListener{
                item.statename.let {
                    listner.getValue(it)
                }

            }
            binding.executePendingBindings()
        }
    }


}
package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.data.model.BottomSheetStateListModelWithId
import com.big9.app.databinding.BottomStateListWithIdBinding

import com.big9.app.utils.`interface`.CallBack2

class BottomStateListAdapterWithId(private val operatorList: ArrayList<BottomSheetStateListModelWithId>, val listner: CallBack2) : RecyclerView.Adapter<BottomStateListAdapterWithId.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BottomStateListWithIdBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(operatorList[position], position)
    }

    override fun getItemCount(): Int {
        return operatorList.size
    }


    inner class MyViewHolder(val binding: BottomStateListWithIdBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BottomSheetStateListModelWithId, position: Int) {
            binding.model=item
            ///*if (position!=items.size-1) {

            //}*/
            /*item.title.let {
              //  binding.tvState.text = it
            }*/

            binding.clHeader.setOnClickListener{
                item.statename.let {
                    listner.getValue2(it,item.id.toString())
                }

            }
            binding.executePendingBindings()
        }
    }


}
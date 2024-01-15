package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.big9.app.databinding.OperatorListBinding
import com.big9.app.data.model.OperatorModel

import com.big9.app.utils.`interface`.CallBack7

class OperatorAdapter(
    var operatorList: ArrayList<OperatorModel>,

    val listner2: CallBack7

) : RecyclerView.Adapter<OperatorAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = OperatorListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(operatorList[position], position)
    }

    override fun getItemCount(): Int {
        return operatorList.size
    }


    inner class MyViewHolder(val binding: OperatorListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OperatorModel, position: Int) {
            binding.model = item
            ///*if (position!=items.size-1) {

            //}*/
            item.title.let {
                //  binding.tvState.text = it
            }
            item.image?.let { image ->
                Glide.with(binding.imgOperatorLogo.context)
                    .load(image)
                    .into(binding.imgOperatorLogo)
                //binding.imgOperatorLogo.setImageResource(image)
                //binding.imgOperatorLogo.setBackgroundResource(item.image)
            }
            binding.clHeader.setOnClickListener {
                listner2.getValue7(s1=item.opcode.toString(),s2=item.minlen.toString(),s3=item.maxlen.toString(),
                s4=item.minrecharge.toString(),s5=item.maxrecharge.toString(),item.title.toString(),item.image.toString()
                )

            }
            binding.executePendingBindings()
        }
    }


}
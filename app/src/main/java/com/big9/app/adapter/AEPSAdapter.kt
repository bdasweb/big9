package com.big9.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.AepsLayoutBinding
import com.big9.app.data.model.ListIcon

import com.big9.app.utils.`interface`.CallBack2


class AEPSAdapter(private val items: List<ListIcon>, val circleShape: Int, val callBack: CallBack2) : RecyclerView.Adapter<AEPSAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        /*val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_items, parent, false)
        return MyViewHolder(view)*/
       /* val binding = BannerItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)*/


        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: AepsLayoutBinding = AepsLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: AepsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListIcon, position: Int) {
            ///*if (position!=items.size-1) {
                binding.imgIcon.setBackgroundResource(circleShape)
            //}*/
            item.title?.let {title->
                binding.tvTitle.text = title
                binding.llContent.setOnClickListener{
                    callBack.getValue2(title,item.slag.toString())
                }
            }
            item.image?.let {image->
                binding.imgIcon.setImageResource(image)
            }

        }
    }


}
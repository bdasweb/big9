package com.big9.app.adapter.reportAdapter



import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.data.model.allReport.CommissionReportData
import com.big9.app.databinding.CommissionReportLayoutItemBinding
import com.big9.app.utils.`interface`.CallBack


class CommissionReportAdapter(

    var items: ArrayList<CommissionReportData>,

    val callBack: CallBack
) : RecyclerView.Adapter<CommissionReportAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
/*val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_items, parent, false)
        return MyViewHolder(view)*/

/* val binding = BannerItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)*/


        //commission_report_layout_item
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CommissionReportLayoutItemBinding =
            CommissionReportLayoutItemBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)


    }




    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: CommissionReportLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommissionReportData, position: Int) {
            binding.apply {
                Log.d("TAG_item", "bind: "+item.comm)
                model=item

               // propertyModel=reportPropertyModel
                executePendingBindings()
            }

        }
    }


}

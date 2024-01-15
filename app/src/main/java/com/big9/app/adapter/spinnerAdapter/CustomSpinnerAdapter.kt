package com.big9.app.adapter.spinnerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.big9.app.R

class CustomSpinnerAdapter(private val context: Context, private val dataList: Array<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            // Inflate the layout for each list item
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.custom_spinner_item_2, null)

            // Create a ViewHolder to store references to the views for efficient recycling
            viewHolder = ViewHolder()
            viewHolder.textView = view.findViewById(R.id.tv_items)

            // Set the ViewHolder as a tag for the view
            view.tag = viewHolder
        } else {
            // View is being recycled, retrieve the ViewHolder from the tag
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Set the data to the views
        viewHolder.textView.text = dataList[position]

        return view
    }

    // ViewHolder class to hold references to the views for efficient recycling
    private class ViewHolder {
        lateinit var textView: TextView
    }
}
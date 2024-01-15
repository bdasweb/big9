package com.big9.app.adapter.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.databinding.DeviceListItemsBinding
import com.big9.app.utils.`interface`.DeviceNameOnClickListner



class BluetoothDeviceAdapter(
    var bluetoothDeviceList: ArrayList<BluetoothDevice>,
    private val deviceNameOnClickListner: DeviceNameOnClickListner
):RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BluetoothDeviceAdapter.ViewHolder {
        val binding = DeviceListItemsBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BluetoothDeviceAdapter.ViewHolder, position: Int) {
        val item=bluetoothDeviceList.get(position)
        holder.bind(item)
        holder.itemView.setOnClickListener{
            Toast.makeText(holder.itemView.context, "", Toast.LENGTH_SHORT).show()
            deviceNameOnClickListner.showDevice(item)
        }
    }

    override fun getItemCount(): Int=bluetoothDeviceList.size
    class ViewHolder(private val binding: DeviceListItemsBinding):RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("MissingPermission")
        fun bind(item: BluetoothDevice) {
            item.name?.let {deviceName->
                binding.tvDevice.text = deviceName
            }

        }

    }


}
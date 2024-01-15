package com.big9.app.adapter.walletledger

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.data.model.allReport.WalletLedgerData
import com.big9.app.databinding.WalletLedgerItemsBindingBinding

class WalletLedgerAdapter : PagingDataAdapter<WalletLedgerData, WalletLedgerAdapter.WalletLedgerViewHolder>(
    WalletLedgerDataDiffCallback()
) {
//wallet_ledger_items_binding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletLedgerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WalletLedgerItemsBindingBinding.inflate(inflater, parent, false)
        return WalletLedgerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalletLedgerViewHolder, position: Int) {
        val walletLedgerData = getItem(position)
        walletLedgerData?.let {
            holder.bind(it)
        }
    }

    inner class WalletLedgerViewHolder(private val binding: WalletLedgerItemsBindingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(walletLedgerData: WalletLedgerData) {
            binding.walletLedgerData = walletLedgerData
            Log.d("TAG_paging", "bind: ")
            binding.executePendingBindings()
        }
    }
}

class WalletLedgerDataDiffCallback : DiffUtil.ItemCallback<WalletLedgerData>() {
    override fun areItemsTheSame(oldItem: WalletLedgerData, newItem: WalletLedgerData): Boolean {
        // Assuming your WalletLedgerData has a unique identifier called "id"
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WalletLedgerData, newItem: WalletLedgerData): Boolean {
        // Check if the content is the same, this depends on your business logic
        return oldItem == newItem
    }
}
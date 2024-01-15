package com.big9.app.ui.popup

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.big9.app.databinding.PopupBalenceBinding
import com.big9.app.databinding.PopupDebitAlertBinding
import com.big9.app.databinding.PopupDthUserDetailsBinding
import com.big9.app.utils.`interface`.CallBack


object CustomPopup {

    fun showBindingPopup(context: Context/*, userInfoModel: UserInfoModel*/) {
        val binding = PopupDthUserDetailsBinding.inflate(LayoutInflater.from(context))

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(binding.root)
        //binding.model=userInfoModel


        /*alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }*/

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        binding.imgClose.setOnClickListener {
            alertDialog.dismiss()
        }
    }
    fun showDebitPopup(
        context: Context,/*, userInfoModel: UserInfoModel*/
        callBack: CallBack
    ) {
        val binding = PopupDebitAlertBinding.inflate(LayoutInflater.from(context))
        //popup_debit_alert
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(binding.root)
        //binding.model=userInfoModel


        /*alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }*/

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        binding.btnOk.setOnClickListener {
            alertDialog.dismiss()
            callBack.getValue("ok")
        }
        alertDialog.show()
    }



    fun showBalencePopup(
        context: Context,
        currBalance: String?,
        payoutBalance: String?,
        lienbal: String?,
        lienbalPayout: String?
    ) {
        val binding = PopupBalenceBinding.inflate(LayoutInflater.from(context))
        //popup_balence
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(binding.root)
        binding.tvMainbalance.text=currBalance
        binding.cashoutBalance.text=payoutBalance
        if ((lienbal?.trim().equals("0.00")==true) || (lienbal?.trim().equals("0")==true)){
            binding.tvLeanbalence.visibility= View.INVISIBLE
        }
        if ((lienbalPayout?.trim().equals("0.00")==true) || (lienbal?.trim().equals("0")==true)){
            binding.lienbalPayout.visibility= View.INVISIBLE
        }

        binding.tvLeanbalence.text="(Line Amount: $lienbal)"
        binding.lienbalPayout.text="(Line Amount: $lienbalPayout)"
        //binding.model=userInfoModel


        /*alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }*/

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        binding.imgClose.setOnClickListener {
            alertDialog.dismiss()
        }
    }


}
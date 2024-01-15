package com.big9.app.ui.popup

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import com.big9.app.R


class LoadingPopup(private val activity: Activity?) {
    private var dialog: Dialog? = null
    private var showing = false


    fun show() {


        try {
            if (activity?.isFinishing == false) {

                if (dialog?.isShowing != true) {
                    dialog = Dialog(activity)
                    dialog?.setCancelable(false)
                    dialog?.window
                        ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog?.setContentView(R.layout.custom_progress_bar)
                    dialog?.show()
                    Log.e("loader", "showProgressDialog: ")
                }



            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("error_loader", e.message!!)
        }
    }


    fun dismiss() {
        try {
            if (dialog != null && !activity!!.isFinishing) {
                dialog?.dismiss()
                Log.e("hide_progress", "hideProgressDialog: ")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
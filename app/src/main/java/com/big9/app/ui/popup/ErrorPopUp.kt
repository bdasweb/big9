package com.big9.app.ui.popup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.big9.app.R


class ErrorPopUp(val context: Context) {

    private var dialog: Dialog = Dialog(context)

    fun showMessageDialog(desc: String?) {
        if (!dialog.isShowing) {
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.error_dialog)
            dialog.window?.setGravity(Gravity.CENTER)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            val tvError = dialog.findViewById<TextView>(R.id.tvError)
            tvError.text = desc
            val report = dialog.findViewById<TextView>(R.id.report)
            report.setOnClickListener {
                dialog.dismiss()
            }
            // final DecimalFormat df = new DecimalFormat("0.00");
            dialog.show()
        }

    }

    fun dismissDialog() {
        if (dialog.isShowing) {
            dialog.dismiss()
            dialog.cancel()
        }
    }

}
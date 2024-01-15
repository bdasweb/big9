package com.big9.app.ui.base

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import com.big9.app.R
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.DecimalDigitsInputFilter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheetFullPageFragment: BottomSheetDialogFragment() {

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme // Use a custom style to make the dialog full-screen
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.let { dialog ->
            dialog.setOnShowListener {
                val bottomSheetDialog = dialog as BottomSheetDialog
                val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.peekHeight = 0
                }
            }
        }
    }
    fun EditText.setupAmount() {
        /*val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            // Define your filter logic here
            val newText = dest.toString().substring(0, dstart) + source.subSequence(start, end) + dest.toString().substring(dend)
            if (newText.matches(Regex("^\\d{0,7}(\\.\\d{0,2})?\$"))) {
                null // Accept the input
            } else {
                "" // Reject the input
            }
        }*/

        //this.filters = arrayOf(inputFilter)
        this.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(Constants.INPUT_FILTER_MAX_VALUE, Constants.INPUT_FILTER_POINTER_LENGTH))

    }

}
package com.big9.app.ui.base

import android.text.InputFilter
import android.widget.EditText
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.DecimalDigitsInputFilter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheetFragment: BottomSheetDialogFragment() {
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
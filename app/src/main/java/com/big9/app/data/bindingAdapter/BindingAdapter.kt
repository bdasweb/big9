package com.big9.app.data.bindingAdapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.big9.app.R

import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.data.viewMovel.MyViewModel


@BindingAdapter("app:hideErrorTextView")

fun hideErrorTextView(view: TextView, isVisible: Boolean) {
    if (isVisible)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.INVISIBLE
}


@BindingAdapter("app:requestFocus")
fun requestFocus(view: EditText, requestFocus: Boolean) {
    if (requestFocus)
        view.requestFocus()
}
@BindingAdapter("app:addTextChangeListener")
fun addTextChangeListener(view: EditText, viewModel: ViewModel) {

    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            when (viewModel) {
                is AuthViewModel -> viewModel.invisibleErrorTexts()
                is MyViewModel -> viewModel.invisibleErrorTexts()

            }
        }

    })

}


@BindingAdapter("app:setImage")
fun setImage(view: ImageView, imageInt: Int) {
    if (imageInt!=null) {
        view.setImageResource(imageInt)
    }
    else{
        view.setImageResource(R.drawable.default_1)
    }
}

@BindingAdapter("app:setApprovedImage")
fun setApprovedImage(view: ImageView, isApproved: Boolean) {
    if (isApproved) {
        view.setImageResource(R.drawable.right_tick)
    }
    else{
        view.setImageResource(R.drawable.close_icon)
    }
}


@BindingAdapter("app:textColorBasedOnStatus")
fun textColorBasedOnStatus(textView: TextView, statusString: String) {
    if (statusString.lowercase().trim()=="failed" || statusString.lowercase().trim().contains("fail")) {
        textView.setColor(R.color.white,R.drawable.btn_red_bg)

    }

    else if (statusString.lowercase().trim()=="success") {
        textView.setColor(R.color.white,R.drawable.btn_green_bg)
    }
    else{
        textView.setColor(R.color.white,R.drawable.btn_bg)
    }
}


fun TextView.setColor(textColor: Int, bgColor: Int) {
    this.setBackgroundResource(bgColor)
    val textColor = ContextCompat.getColor(this.context, textColor)
    this.setTextColor(textColor)
}






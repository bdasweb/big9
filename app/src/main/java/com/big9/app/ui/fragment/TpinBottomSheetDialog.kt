package com.big9.app.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.R

import com.big9.app.adapter.PhonePadAdapter
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.TpinBottomsheetLayoutBinding

import com.big9.app.ui.base.BaseBottomSheetFragment
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.KeyPadOnClickListner

class TpinBottomSheetDialog(val callBack: CallBack) : BaseBottomSheetFragment() {
    lateinit var binding: TpinBottomsheetLayoutBinding
    private val myViewModel: MyViewModel by activityViewModels()
    var keyPad = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.tpin_bottomsheet_layout, container, false)
        binding.viewModel = myViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun getTheme(): Int {
        return R.style.SheetDialog
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    override fun onResume() {
        super.onResume()
        myViewModel.tPin.value=""
    }
    private fun onViewClick() {
        binding.apply {}

    }

    private fun setObserver() {

    }

    private fun initView() {
        binding.apply {
            setKeyPad(recyclePhonePad)
        }

    }

    fun setKeyPad(PhonePad: RecyclerView) {
        keyPad.clear()
        keyPad.add(1)
        keyPad.add(2)
        keyPad.add(3)
        keyPad.add(4)
        keyPad.add(5)
        keyPad.add(6)
        keyPad.add(7)
        keyPad.add(8)
        keyPad.add(9)
        keyPad.add(10)
        keyPad.add(0)
        keyPad.add(11)
        PhonePad.apply {

            adapter= PhonePadAdapter(keyPad,object : KeyPadOnClickListner {
                override fun onClick(item: Int) {
                    myViewModel.tPin.value?.apply {
                        if (item<=9 ) {
                            if (this.length!=6) {
                                val tPin="${this}$item"
                                myViewModel.tPin.value= tPin
                                if(tPin.length==6){
                                    callBack.getValue(tPin)
                                    dismiss()
                                }

                            }
                        }

                        else {
                            if (this.isNotEmpty()) {
                                myViewModel.tPin.value = this.toString().substring(0, this.length - 1)

                            }

                        }
                    }
                }

            })
            isNestedScrollingEnabled=false
        }
    }

}
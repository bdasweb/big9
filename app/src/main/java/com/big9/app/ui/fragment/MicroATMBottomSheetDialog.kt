package com.big9.app.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.MatmBottomsheetLayoutBinding

import com.big9.app.ui.base.BaseBottomSheetFragment
import com.big9.app.utils.`interface`.CallBack

class MicroATMBottomSheetDialog(val callBack: CallBack) : BaseBottomSheetFragment() {
    lateinit var binding: MatmBottomsheetLayoutBinding
    private val myViewModel: MyViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.matm_bottomsheet_layout, container, false)
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

    private fun onViewClick() {
        binding.apply {
            imgBalanceEnquary.setOnClickListener{
                callBack.getValue("balence_enquery")
                dismiss()
            }
            imgCashwithdraw.setOnClickListener{
                callBack.getValue("balence_withdraw")
                dismiss()
            }

        }

    }

    private fun setObserver() {

    }

    private fun initView() {
        binding.apply {

        }

    }



}
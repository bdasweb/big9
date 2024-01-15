package com.big9.app.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.MoveToBottomsheetLayoutBinding

import com.big9.app.ui.base.BaseBottomSheetFragment
import com.big9.app.utils.`interface`.CallBack

class SelectTransactionTypeBottomSheetDialog(val callBack: CallBack) : BaseBottomSheetFragment() {
    lateinit var binding: MoveToBottomsheetLayoutBinding
    private val myViewModel: MyViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.move_to_bottomsheet_layout, container, false)
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
        myViewModel?.amtMoveToBank?.value=""
    }

    private fun onViewClick() {
        binding.apply {
            etAmtBankTranspher.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    tvMoveToBankBottomsheetSubmit.performClick()
                    return@setOnEditorActionListener true
                }
                false
            }
            tvMoveToBankBottomsheetSubmit.setOnClickListener {
                viewModel?.apply {
                    if (MoveToBankValidation()) {
                        amtMoveToBank?.value?.let { it1 -> callBack.getValue(it1) }
                        dismiss()
                    }
                }
            }
        }

    }

    private fun setObserver() {

    }

    private fun initView() {
        binding.apply {
            viewModel?.tPin?.value=""
            etAmtBankTranspher.setupAmount()
            viewModel?.IMPSIsActive?.value=false
            viewModel?.NEFTIsActive?.value=false
            llNEFT.setOnClickListener{
                viewModel?.IMPSIsActive?.value=false
                viewModel?.NEFTIsActive?.value=true
            }

            llIMPS.setOnClickListener{
                viewModel?.IMPSIsActive?.value=true
                viewModel?.NEFTIsActive?.value=false
            }
            imgClose.setOnClickListener{
                dismiss()
            }
        }

    }


}
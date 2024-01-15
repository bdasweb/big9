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

import com.big9.app.adapter.FastTagBankListAdapter
import com.big9.app.data.model.FastTagBankListModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FasttagOperatorListBottomsheetLayoutBinding

import com.big9.app.ui.base.BaseBottomSheetFragment
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4

class FasttagOperatorListBottomSheetDialog(val callBack: CallBack) : BaseBottomSheetFragment() {
    lateinit var binding: FasttagOperatorListBottomsheetLayoutBinding
    private val myViewModel: MyViewModel by activityViewModels()
    var fastTagBankList = ArrayList<FastTagBankListModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fasttag_operator_list_bottomsheet_layout, container, false)
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
        binding.apply {}

    }

    private fun setObserver() {

    }

    private fun initView() {
        binding.apply {
            binding.apply {
                recycleViewPaymentRequest.apply {
                    fastTagBankList.add(FastTagBankListModel(R.drawable.axix_bank_logo,"Axis Bank FASTag"))
                    fastTagBankList.add(FastTagBankListModel(R.drawable.icici,"ICICI Bank FASTag"))
                    adapter= FastTagBankListAdapter(fastTagBankList, object : CallBack4 {
                        override fun getValue4(s1: String, s2: String, s3: String, s4: String) {
                            viewModel?.fastTagOperator?.value=s1
                            dismiss()
                        }




                    })
                }
            }
        }

    }


}
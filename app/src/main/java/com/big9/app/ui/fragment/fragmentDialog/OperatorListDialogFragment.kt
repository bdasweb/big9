package com.big9.app.ui.fragment.fragmentDialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.adapter.OperatorListAdapter
import com.big9.app.data.model.OperatorListModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentOperatorlistDialogBinding

import com.big9.app.ui.base.BaseCenterSheetFragment
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4


class OperatorListDialogFragment(val callBack: CallBack) : BaseCenterSheetFragment() {
    lateinit var binding: FragmentOperatorlistDialogBinding
    private val viewModel: MyViewModel by activityViewModels()
    var operatorListModel = ArrayList<OperatorListModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_operatorlist_dialog, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {
        binding.apply {
            imgBack.setOnClickListener{
                dismiss()
            }

          }

        }


    fun initView() {
        setCrdViewMinHeight()
        binding.recycleViewOperatpr.apply {
            operatorListModel.add(OperatorListModel(R.drawable.airtel_com_logo,"Airtel"))
            operatorListModel.add(OperatorListModel(R.drawable.gtlp,"GTPL"))
            operatorListModel.add(OperatorListModel(R.drawable.bsnl_broadband_logo,"BSNL"))
            operatorListModel.add(OperatorListModel(R.drawable.bsnl_broadband_logo,"BSNL Landline Corporate"))
            operatorListModel.add(OperatorListModel(R.drawable.allance_broadband,"Alliance Broadband"))
            adapter= OperatorListAdapter(operatorListModel, object : CallBack4 {
                override fun getValue4(s1: String, s2: String, s3: String, s4: String) {
                    viewModel?.selectOperator?.value=s1
                    try {
                        viewModel?.selectImage?.value=s2.toInt()
                    }catch (e:Exception){}


                    dismiss()
                }
            })
        }

    }

    private fun setCrdViewMinHeight() {
    }

    fun setObserver() {
        binding.apply {

        }

    }
}
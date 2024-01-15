package com.big9.app.ui.fragment.billpay


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.big9.app.R
import com.big9.app.databinding.BillpayFragmentBinding

import com.big9.app.ui.base.BaseFragment

class BillPayFragment : BaseFragment() {
    lateinit var binding: BillpayFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.billpay_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
    }

    fun initView() {

    }

    fun setObserver() {

    }
}
package com.big9.app.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.adapter.reportAdapter.ReportAdapter
import com.big9.app.data.model.ReportModel
import com.big9.app.data.model.ReportPropertyModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentMiniStatementBinding

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.`interface`.CallBack

class MiniStatementFragment : BaseFragment() {
    lateinit var binding: FragmentMiniStatementBinding
    private val viewModel: MyViewModel by activityViewModels()
    var reportList = ArrayList<ReportModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mini_statement, container, false)
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
          imgBack.back()

          tvStartDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.startDate?.value=s
                    }

                })
            }

            tvEndDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.enddate?.value=s
                    }

                })
            }

            recycleViewReport.apply {
                reportList.clear()

                reportList.add(
                    ReportModel(
                        "001",
                        "778.00",
                        "10-10-2023",
                        "Refunded",
                        0,
                        desc = "Rajiv\nA/c No.:111111111111\nSender: 5555555555",
                        imageInt = R.drawable.imps_logo,
                        image1 = 2,
                        isClickAble = true
                    )
                )
                reportList.add(
                    ReportModel(
                        "002",
                        "778.00",
                        "10-10-2023",
                        getString(R.string.success),
                        1,
                        desc = "Jhuma Chowdhary\nA/c No.:000000000000\nSender :8888888888",
                        imageInt = R.drawable.imps_logo,
                        image1 = 2
                    )
                )




                viewModel?.reportType?.value?.let {type->



                    val reportPropertyModel=   when(type){

                        getString(R.string.payment)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.transactions)->{
                            ReportPropertyModel("Transaction id","")
                        }
                        getString(R.string.dmt)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.load_Requests)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.wallet_ledger)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.cashout_ledger)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.aeps)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.micro_atm)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.commissions)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.bank_settle)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.wallet_settle)->{
                            ReportPropertyModel("Transaction id")
                        }
                        getString(R.string.complaints)->{
                            ReportPropertyModel("Transaction id")
                        }

                        else -> {
                            ReportPropertyModel("Transaction id")
                        }
                    }
                    if (reportList.size>0){
                        binding.tvNoDataFound.visibility=View.GONE
                    }else{
                        binding.tvNoDataFound.visibility=View.VISIBLE

                    }
                    binding.nsv.isVisible=!tvNoDataFound.isVisible
                    adapter= ReportAdapter(reportPropertyModel,reportList,  object : CallBack {
                        override fun getValue(s: String) {
                            /*val bundle = Bundle()
                            bundle.putString("jsonData", s)
                            findNavController().navigate(R.id.action_reportFragment_to_reportDetailsFragment,bundle)*/
                        }
                    })
                }

            }
        }
    }

    fun initView() {
        viewModel?.apply {
            startDate.value ="".currentdate()
            enddate.value="".currentdate()
        }

    }

    fun setObserver() {
        binding.apply {

        }

    }
}
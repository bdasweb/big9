package com.big9.app.ui.fragment.paymentRequest


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.adapter.BankModeListAdapter
import com.big9.app.data.model.BankModeListModel
import com.big9.app.data.model.PaymentREquistModeModelData
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentPaymentModeBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack2
import com.google.gson.Gson

class PaymentRequestModeFragment : BaseFragment() {
    lateinit var binding: FragmentPaymentModeBinding
    private val viewModel: MyViewModel by activityViewModels()
    var bankInformationList = ArrayList<BankModeListModel>()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_mode, container, false)
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

        }



    }

    fun initView() {
        binding.imgBack.back()
        binding.apply {
            viewModel?.paySleeyUriUri=null
            viewModel?.denomSlipUriUri=null
            activity?.let {act->
                        loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
            }
            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            loginResponse?.let { loginData ->
                loginData.userid?.let {
                    val data = mapOf(
                        "userid" to loginData.userid,
                        "bankid" to viewModel?.selectedBankId?.value
                    )
                    val gson =  Gson()
                    var jsonString = gson.toJson(data);
                    loginData.AuthToken?.let {
                        viewModel?.PaymentREquistMode(it,jsonString.encrypt())
                    }
                }
            }

        }
    }

    fun setObserver() {
        viewModel?.PaymentREquistModeResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    it?.data?.data?.let {

                            setRecycleView(it)

                    }
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }

    }

    private fun setRecycleView(allBankInformationList: ArrayList<PaymentREquistModeModelData>) {
        binding?.apply {
            recycleViewPaymentMode.apply {
                bankInformationList.clear()
                if(allBankInformationList.size>0){
                   val data= allBankInformationList.get(0)
                    data.apply {
                        viewModel?.selectedBank?.value=data.bankName
                        if (cashcounterDeposit.toString().isActive()){
                            bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"CASH - COUNTER DEPOSIT","Minimam Amount:${cashcounterDepositMinAmount}","Maximum Amount: ${cashcounterDepositMaxAmount}"))
                        }
                        if (cashCdm.toString().isActive()){
                            bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"CASH CDM","Minimam Amount:${cashCdmMinAmount}","Maximum Amount: ${cashCdmMaxAmount}"))
                        }
                        if (credit.toString().isActive()){
                            bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"CREDIT","Minimam Amount:${creditMinAmount}","Maximum Amount: ${creditMaxAmount}"))
                        }
                        if (atm.toString().isActive()){
                            bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ATM","Minimam Amount:${atmMinAmount}","Maximum Amount: ${atmMaxAmount}"))
                        }
                        if (cheque.toString().isActive()){
                            bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"CHEQUE","Minimam Amount:${chequeMinAmount}","Maximum Amount: ${chequeMaxAmount}"))
                        }
                        if (onlineSameBank.toString().isActive()){
                            bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE SAME BANK","Minimam Amount:${onlineSameBankMinAmount}","Maximum Amount: ${onlineSameBankMaxAmount}"))
                        }
                        if (onlineImps.toString().isActive()){
                            bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE IMPS","Minimam Amount:${onlineImpsMinAmount}","Maximum Amount: ${onlineImpsMaxAmount}"))
                        }
                        if (onlineNeft.toString().isActive()){
                            bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE NEFT","Minimam Amount:${onlineNeftMinAmount}","Maximum Amount: ${onlineNeftMaxAmount}"))
                        }
                        if (onlineRtgs.toString().isActive()){
                            bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE RTGS","Minimam Amount:${onlineRtgsMinAmount}","Maximum Amount: ${onlineRtgsMaxAmount}"))
                        }

                    }

                }





               /* bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE SAME BANK","Minimam Amount:100.00","Maximum Amount: 500000.00"))
                bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE - IMPS","Minimam Amount:100.00","Maximum Amount: 500000.00"))
                bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE - NEFT","Minimam Amount:100.00","Maximum Amount: 500000.00"))
                bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE - RTGS","Minimam Amount:100.00","Maximum Amount: 500000.00"))*/
                adapter= BankModeListAdapter(bankInformationList, object : CallBack2 {
                    override fun getValue2(s: String,s2: String) {
                        viewModel?.selectedBankMode?.value=s
                        viewModel?.minMaxBalence?.value=s2
                        findNavController().navigate(R.id.action_paymentRequestModeFragment_to_paymentRequestImformationFragment)
                    }

                })
            }
        }

    }
    fun String.isActive():Boolean{
        return this.trim()=="active"
    }
}
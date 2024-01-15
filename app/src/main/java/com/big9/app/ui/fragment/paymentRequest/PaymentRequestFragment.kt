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
import com.big9.app.adapter.AllBankListAdapter

import com.big9.app.data.model.AllBankListData
import com.big9.app.data.model.BankListModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentPaymentRequestBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson

class PaymentRequestFragment : BaseFragment() {
    lateinit var binding: FragmentPaymentRequestBinding
    private val viewModel: MyViewModel by activityViewModels()
    var bankList = ArrayList<BankListModel>()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_request, container, false)
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

        activity?.let {act->
                    loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            loginResponse?.let { loginData ->
                loginData.userid?.let {
                    val data = mapOf(
                        "userid" to loginData.userid
                    )
                    val gson =  Gson()
                    var jsonString = gson.toJson(data);
                    loginData.AuthToken?.let {
                        viewModel?.bankList(it,jsonString.encrypt())
                    }
                }
            }
        }

    }

    fun setObserver() {
        viewModel?.bankListResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    it.data?.data?.let {
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

    private fun setRecycleView(allBankList: ArrayList<AllBankListData>) {
        binding.apply {
            recycleViewPaymentRequest.apply {
               /* bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))*/
                adapter= AllBankListAdapter(allBankList, object : CallBack4 {
                    override fun getValue4(s1: String, s2: String, s3: String, s4: String) {
                        viewModel?.selectedBank?.value=s1
                        viewModel?.selectedBankId?.value=s3
                        findNavController().navigate(R.id.action_paymentRequestFragment_to_paymentRequestModeFragment)
                    }
                })
            }
        }
    }


}
package com.big9.app.ui.fragment


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.big9.app.BuildConfig

import com.big9.app.R
import com.big9.app.data.model.ReceiptModel
import com.big9.app.data.viewMovel.DTHViewModel

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentDthRechargeBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.activity.DashboardActivity

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.ui.receipt.newRecept.DTHnewMobileReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.isDthOperator
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.Objects

class DTHRechargeFragment : BaseFragment() {
    lateinit var binding: FragmentDthRechargeBinding
    private val viewModel: MyViewModel by activityViewModels()
    private val dthViewModel: DTHViewModel by viewModels()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dth_recharge, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initView()
            setObserver()
            onViewClick()
        }
    }

    private fun onViewClick() {
        binding.apply {

            imgBack.back()
            rootView.setOnClickListener {
                activity?.let { act -> rootView.hideSoftKeyBoard(act) }
            }
            btnCustomerInfo.setOnClickListener{
                activity?.let {
                    loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "operator" to viewModel?.operator?.value,
                                "opcode" to viewModel?.operatorCode?.value,
                                "subscriber_id" to viewModel?.subId?.value,
                                "opname" to viewModel?.dthOperator?.value,

                            )
                            val gson= Gson()

                                var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                dthViewModel?.dthUserInfo(it, jsonString.encrypt())
                            }

                        }
                    }

                }

               /* context?.let {ctx->
                    viewModel?.userName?.value = "Sample user"
                    viewModel?.balence?.value = "1009"
                    viewModel?.nextRecharge?.value = "01-01-2023"
                    viewModel?.monthly?.value = "789"
                    viewModel?.type?.value = "Active"
                    binding.cardUserDetails.visibility=View.VISIBLE
                }*/
            }

            btnSubmit.setOnClickListener{
                if (viewModel?.dthValidation() == true){
                    btnSubmit.setBottonLoader(false,llSubmitLoader)
                    val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                        override fun getValue(s: String) {
                            submit(s)

                        }
                    })
                    activity?.let {act->
                       // tpinBottomSheetDialog.isCancelable = false
                        tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)
                    }
                }
            }
            etOperator.setOnClickListener {
                rlOperator.performClick()
            }
            rlOperator.setOnClickListener{
                activity?.let {act->
                    isDthOperator=true
                    findNavController().navigate(R.id.action_DTHRechargeFragment_to_operatorFragment)
                }

            }
        }

        binding.imgClose.setOnClickListener{
            binding.cardUserDetails.visibility=View.GONE
        }

    }

    fun initView() {
        backPressedCheck()
        binding.apply {
            etAmt.setupAmount()
        }

    }
    private fun submit(tpin: String) {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            if (isLogin){
                loginResponse?.let {loginData->
                    val data ="{\"userid\":\"${loginData.userid}\",\"operator\":\"${viewModel.operator.value}\",\"opcode\":\"${viewModel.operatorCode.value}\",\"tpin\":\"${tpin}\",\"subscriber_id\":\"${viewModel.subId.value}\",\"rcamt\":\"${viewModel.dthAmt.value}\",\"IP\":\"${MethodClass.getLocalIPAddress()}\"}"
                    /*val data = mapOf(
                        "userid" to loginData.userid,
                        "operator" to viewModel.operator.value,
                        "opcode" to viewModel.operatorCode.value,
                        "tpin" to tpin,
                        "custno" to viewModel.mobile.value,
                        "rcamt" to viewModel.amt,
                        "IP" to getLocalIPAddress(),
                    )*/
                    /*val gson= Gson()

                        var jsonString = gson.toJson(data)*/
                    loginData.AuthToken?.let {
                            dthViewModel?.dthTransfer(it, data.encrypt())
                    }

                }
            }

        }
    }
    fun setObserver() {
        dthViewModel?.dthTransferResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()

                }

                is ResponseState.Success -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()
                    /*viewModel?.popup_message?.value="Success"
                    val successPopupFragment = SuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {*/
                            /*it.data?.let {
                                val dialogFragment2 = DthReceptDialogFragment(object: CallBack {
                                    override fun getValue(s: String) {
                                        if (Objects.equals(s,"back")) {
                                            findNavController().popBackStack()
                                        }
                                    }
                                },it)
                                dialogFragment2.show(childFragmentManager, dialogFragment2.tag)
                            }*/
                            var dateTime=it?.data?.timestamp
                            it?.data?.data?.let {
                                if (it?.size!! > 0?:0) {


                                    it?.get(0).let {
                                        viewModel?.receiveStatus?.value = getString(R.string.dth_recharged)
                                        /*
         @SerializedName("userid") var userid: String? = null,
            @SerializedName("status") var status: String? = null,
            @SerializedName("refillid") var refillid: String? = null,
            @SerializedName("subscriber_id") var subscriberId: String? = null,
            @SerializedName("amount") var amount: String? = null,
            @SerializedName("curramt") var curramt: String? = null,
            @SerializedName("operator") var operator: String? = null,
            @SerializedName("operatorid") var operatorid: String? = null
         */
                                        Constants.recycleViewReceiptList.add(
                                            ReceiptModel(
                                                "Transaction Id",
                                                it?.refillid.toString()
                                            )
                                        )
                                        Constants.recycleViewReceiptList.add(
                                            ReceiptModel(
                                                "Operator",
                                                viewModel?.operator?.value.toString()
                                            )
                                        )
                                        Constants.recycleViewReceiptList.add(
                                            ReceiptModel(
                                                "Subscriber Id",
                                                it?.subscriberId.toString()
                                            )
                                        )
                                        Constants.recycleViewReceiptList.add(
                                            ReceiptModel(
                                                "Amount",
                                                it?.amount.toString()
                                            )
                                        )
                                        Constants.recycleViewReceiptList.add(ReceiptModel("Date and Time",dateTime.toString().replace("null","")))
                                        Constants.recycleViewReceiptList.add(
                                            ReceiptModel(
                                                "Status",
                                                it?.status.toString()
                                            )
                                        )


                                        val dialogFragment = DTHnewMobileReceptDialogFragment()
                                        dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                    }
                                }
                            }


                   /*     }
                    }
                    )
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)*/


                    viewModel?.apply {
                        subId.value=""
                        dthOperator.value=""
                        dthAmt.value=""
                    }
                    dthViewModel?.dthTransferResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    dthViewModel?.dthTransferResponseLiveData?.value=null
                }
            }
        }

        dthViewModel?.dthUserInfoResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    context?.let { ctx ->
                        it?.data?.data?.get(0)?.let {


                        viewModel?.userName?.value = it.customerName
                                viewModel?.balence?.value = it.Balance
                        viewModel?.nextRecharge?.value = it.NextRechargeDate
                        viewModel?.monthly?.value = it.MonthlyRecharge
                        viewModel?.type?.value = it.status
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.cardUserDetails.visibility = View.VISIBLE
                        },100)

                    }
                    }


                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


    }

    fun backPressedCheck(){
        activity?.let {act->
            act.onBackPressedDispatcher.addCallback(act, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.let {act->
                        startActivity(Intent(act, DashboardActivity::class.java).putExtra(Constants.isAfterReg,true))
                    }

                }
            })
        }

    }
}
package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import billpaytransactionData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.big9.app.R
import com.big9.app.data.model.ReceiptModel

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentUtilityBillPaymentBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.ui.receipt.ElectricReceptDialogFragment
import com.big9.app.ui.receipt.newRecept.ElectricReceptDialogNewFragment
import com.big9.app.ui.receipt.newRecept.PrePaidMobileReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.Objects

class UtilityBillPaymentFragment : BaseFragment() {
    lateinit var binding: FragmentUtilityBillPaymentBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_utility_bill_payment,
            container,
            false
        )
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
            llRootView.setOnClickListener {
                activity?.let { act -> llRootView.hideSoftKeyBoard(act) }
            }
            rootView.setOnClickListener {
                activity?.let { act -> rootView.hideSoftKeyBoard(act) }
            }

            imgBack.back()

            btnFetchAmt.setOnClickListener {
                if (viewModel?.electricVerifyValidation() == true) {
                    activity?.let { act ->
                        val (isLogin, loginResponse) = sharedPreff.getLoginData()
                        loginResponse?.let { loginData ->
                            loginData.userid?.let {
                                val data = mapOf(
                                    "userid" to loginData.userid,
                                    "custid" to viewModel?.consumerId?.value.toString(),
                                    "opid" to Constants.eOpid
                                )
                                val gson = Gson()
                                var jsonString = gson.toJson(data)
                                loginData.AuthToken?.let {
                                    viewModel?.electricBillbillFetch(it, jsonString.encrypt())
                                }
                            }
                        }
                        //findNavController().navigate(R.id.action_utilityBillPaymentFragment_to_electricPriceListFragment)
                        /*val electricPriceListDialog = ElectricPriceListFragment(object : CallBack {
                            override fun getValue(s: String) {
                                viewModel?.consumerIdPrice?.value=s

                            }

                        })
                        electricPriceListDialog.show(act.supportFragmentManager, electricPriceListDialog.tag)
    */
                    }

                }


            }

            binding.btnSubmit.setOnClickListener {
                if (viewModel?.electricValidation() == true) {
                    binding.btnSubmit.setBottonLoader(false,binding.llSubmitLoader)
                    val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                        override fun getValue(tpin: String) {

                            val (isLogin, loginResponse) = sharedPreff.getLoginData()
                            loginResponse?.let { loginData ->
                                loginData.userid?.let {
                                    /*
                                    {"userid":"100419","custid":"303357803","opid":"71","ipaddress":"","txnAmount":"00","usertpin":"815685"}

                                     */

                                    val data = mapOf(
                                        "userid" to loginData.userid,
                                        "custid" to viewModel?.consumerId?.value.toString(),
                                        "opid" to Constants.eOpid,
                                        "ipaddress" to MethodClass.getLocalIPAddress(),
                                        "txnAmount" to viewModel?.consumerIdPrice?.value,
                                        "usertpin" to tpin,
                                    )
                                   // var data="{\"userid\":\"${loginData.userid}\",\"custid\":\"${viewModel?.consumerId?.value.toString()}\",\"opid\":\"${Constants.eOpid}\",\"ipaddress\":\"${MethodClass.getLocalIPAddress()}\",\"txnAmount\":\"${viewModel?.consumerIdPrice?.value}\",\"usertpin\":\"${tpin}\"}\n"
                                    val gson = Gson()
                                    var jsonString = gson.toJson(data)
                                    loginData.AuthToken?.let {
                                        viewModel?.billpaytransaction(it, jsonString.encrypt())
                                    }
                                }
                            }


                            /**/
                        }
                    })
                    activity?.let { act ->
                        //tpinBottomSheetDialog.isCancelable=false
                        tpinBottomSheetDialog.show(
                            act.supportFragmentManager,
                            tpinBottomSheetDialog.tag
                        )
                    }
                }
            }

        }


    }

    fun initView() {
        activity?.let { act ->
            loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        binding.apply {
            Glide.with(binding.root.context)
                .asGif()
                .load(R.drawable.electric_light_animation_logo)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgGif)
        }

    }

    fun setObserver() {
        viewModel.electricBillbillFetchResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    //viewModel.consumerIdPrice.value = "10"
                    it?.data?.amt?.let {amt->
                        viewModel.consumerIdPrice.value = amt
                    }

                    viewModel.popup_message.value = "${it?.data?.message}"
                    val successPopupFragment = SuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            viewModel.popup_message.value = "Success"
                        }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
                    viewModel.electricBillbillFetchResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel.electricBillbillFetchResponseLiveData?.value=null
                }
            }
        }
        //
        viewModel?.billpaytransactionResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                   // var eTransDAta=it.data?.data
                    it.data?.data?.let {
                        Constants.recycleViewReceiptList.clear()
                        viewModel?.receiveStatus?.value=getString(R.string.payment_receipt)
                        Constants.recycleViewReceiptList.add(ReceiptModel("Transaction Id",it.txnID.toString().replace("null","")))
                        Constants.recycleViewReceiptList.add(ReceiptModel("Customer Id",it.customerId.toString().replace("null","")))
                        Constants.recycleViewReceiptList.add(ReceiptModel("Amount",it.txnAmount.toString().replace("null","")))
                        Constants.recycleViewReceiptList.add(ReceiptModel("Date",it.txnDate.toString().replace("null","")))
                        Constants.recycleViewReceiptList.add(ReceiptModel("Status",it.txnstatue.toString().replace("null","")))
                        Constants.recycleViewReceiptList.add(ReceiptModel("Operator Id",it.operatorid.toString().replace("null","")))
                        //Constants.recycleViewReceiptList.add(ReceiptModel("Refill Id",it.refillid.toString()))
                        //Constants.recycleViewReceiptList.add(ReceiptModel("Status",it.status.toString().replace("null","")))
                        val dialogFragment = ElectricReceptDialogNewFragment()
                        dialogFragment.show(childFragmentManager, dialogFragment.tag)

                    }


                    /*viewModel.popup_message.value = "${it?.data?.message}"
                    val successPopupFragment = SuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            viewModel.popup_message.value = "Success"
                            val dialogFragment = ElectricReceptDialogFragment(object: CallBack {
                                override fun getValue(s: String) {
                                    if (Objects.equals(s,"back")) {
                                        findNavController().popBackStack()
                                    }
                                }
                            },eTransDAta)
                            dialogFragment.show(childFragmentManager, dialogFragment.tag)
                        }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
                    */
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    viewModel?.billpaytransactionResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    viewModel?.billpaytransactionResponseLiveData?.value=null
                }
            }
        }
    }


}
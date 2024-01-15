package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.model.ReceiptModel
import com.big9.app.data.viewMovel.DTHViewModel

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentViewRetailerBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment2
import com.big9.app.ui.receipt.newRecept.PrePaidMobileReceptDialogFragment
import com.big9.app.ui.receipt.newRecept.RetailerPayReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.google.firebase.messaging.remoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.launch

class RetailerPaymentFragment : BaseFragment() {
    lateinit var binding: FragmentViewRetailerBinding
    private val viewModel: MyViewModel by activityViewModels()
    private val dthViewModel: DTHViewModel by viewModels()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_retailer, container, false)
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


            btnSubmit.setOnClickListener {
                if (viewModel?.ratailerPayAmt?.value?.isNotEmpty() == true) {

                    val tpinBottomSheetDialog = TpinBottomSheetDialog(object :
                        CallBack {
                        override fun getValue(tpin: String) {
                            submit(tpin)
                        }
                    })
                    activity?.let {act->
                        tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)
                    }

                }
            }


        }
    }


    fun initView() {

        activity?.let {act->
           loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        backPressedCheck()
        binding?.apply {
            tvUserName.text=Constants.name
            tvUserMobile.text="Mobile No.: ${Constants.mobileNo}"
            tvUserCurrentBalence.text="current balance ${Constants.currBalance}"
        }

    }

    private fun submit(tpin: String) {
        val (isLogin, loginResponse) = sharedPreff.getLoginData()
        loginResponse?.let { loginData ->
            loginData.userid?.let {

                val data = mapOf(
                    "userid" to loginData.userid,
                    "amount" to viewModel?.ratailerPayAmt?.value,
                    "tpin" to tpin,
                    "retailerid" to Constants.ID
                )
                val gson = Gson()
                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {
                    viewModel?.pay_partner(it, jsonString.encrypt())
                }
            }
        }
    }

    fun setObserver() {
        viewModel?.pay_partnerResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    viewModel?.ratailerPayAmt?.value=""
                    /*
                     @SerializedName("amount")
    var amount: String? = null
    @SerializedName("paymentby")
    var paymentby: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("receiveby")
    var receiveby: String? = null
    @SerializedName("lasttransactiontime")
    var lasttransactiontime: String? = null
    @SerializedName("operatorid")
    var operatorid: String? = null
                     */

                    viewModel.popup_message.value="${it?.data?.Description}"
                    val successPopupFragment = SuccessPopupFragment2(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            viewModel.popup_message.value="Success"
                            Constants.recycleViewReceiptList.clear()
                            it?.data?.data?.let {
                                if (it?.size!! > 0 ?: 0) {
                                    it?.get(0)?.let {
                                        Constants.recycleViewReceiptList.add(ReceiptModel("Payment By",it.paymentby.toString()))
                                        Constants.recycleViewReceiptList.add(ReceiptModel("Status",it?.status.toString()))
                                        Constants.recycleViewReceiptList.add(ReceiptModel("Receive By",it.receiveby.toString()))
                                        Constants.recycleViewReceiptList.add(ReceiptModel("Amount",it.amount.toString()))

                                        //Constants.recycleViewReceiptList.add(ReceiptModel("Refill Id",it.refillid.toString()))
                                        Constants.recycleViewReceiptList.add(ReceiptModel("Operator Id",it.operatorid.toString()))
                                        val dialogFragment = RetailerPayReceptDialogFragment()
                                        dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                    }
                                }
                            }


                            viewModel?.pay_partnerResponseLiveData?.value=null
                        }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)







                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    //binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    viewModel?.pay_partnerResponseLiveData?.value=null
                }
            }
        }
    }

    fun backPressedCheck() {}

}

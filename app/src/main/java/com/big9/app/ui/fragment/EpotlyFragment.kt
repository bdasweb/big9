package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.model.ReceiptModel

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentEpotlyBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.ui.receipt.EPotlyReceptDialogFragment
import com.big9.app.ui.receipt.newRecept.EpotlyReceptDialogFragment
import com.big9.app.ui.receipt.newRecept.PrePaidMobileReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.epotlyMoboleNo
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson
import java.util.Objects

class EpotlyFragment : BaseFragment() {
    lateinit var binding: FragmentEpotlyBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_epotly, container, false)
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
            rootView.setOnClickListener {
                activity?.let { act -> rootView.hideSoftKeyBoard(act) }
            }
            imgBack.back()
            btnSubmit.setOnClickListener{
                activity?.let {act->
                    if (viewModel?.epotlyValidation() == true){
                        val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                            override fun getValue(s: String) {
                                val (isLogin, loginResponse) =sharedPreff.getLoginData()
                                if (isLogin){
                                    loginResponse?.let {loginData->
                                        viewModel?.apply {

                                            val  data = mapOf(
                                                "userid" to loginData.userid,
                                                "tpin" to s,
                                                "custno" to epotly_mobile.value,
                                                "amt" to epotly_amt.value
                                            )

                                            val gson= Gson()
                                            var jsonString = gson.toJson(data)
                                            loginData.AuthToken?.let {
                                                epotlyTranspher(it,jsonString.encrypt())
                                            }
                                        }

                                    }
                                }



                            }
                        })
                        tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)

                    }
                }

            }

        }



    }

    fun initView() {
        binding.apply {
            etAmt.setupAmount()
        }
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
    }

    fun setObserver() {
        viewModel?.epotlyTranspherResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    it?.data?.epotlyData?.mobileNo?.let {
                        epotlyMoboleNo=it
                    }
                    it?.data?.epotlyData?.let {
                        sharedPreff.getUserData()?.let { userData ->
                            viewModel?.receiveStatus?.value=getString(R.string.ePotly)
                            Constants.recycleViewReceiptList.clear()
                            viewModel?.receiveStatus?.value=getString(R.string.mobile_recharged)
                            Constants.recycleViewReceiptList.add(ReceiptModel("Transaction",it.id.toString().replace("null","")))
                            Constants.recycleViewReceiptList.add(ReceiptModel("MobileNo",it.mobileNo.toString().replace("null","")))
                            Constants.recycleViewReceiptList.add(ReceiptModel("Last Transaction Amount",it.LastTransactionAmount.toString().replace("null","")))
                            Constants.recycleViewReceiptList.add(ReceiptModel("Status",it.status.toString().replace("null","")))
                            Constants.recycleViewReceiptList.add(ReceiptModel("Date and Time",MethodClass.getCurrentDateTime()))

                            val dialogFragment = EpotlyReceptDialogFragment()
                            dialogFragment.show(childFragmentManager, dialogFragment.tag)
                        }


                    }

                   /* viewModel.popup_message.value="${it?.data?.epotlyData?.status}\nMobile No.:${it?.data?.epotlyData?.mobileNo}\nLast Transaction Amount: ${it?.data?.epotlyData?.LastTransactionAmount}\nBalance : ${it?.data?.epotlyData?.curramt}"
                        val successPopupFragment = SuccessPopupFragment(object :
                            CallBack4 {
                            override fun getValue4(
                                s1: String,
                                s2: String,
                                s3: String,
                                s4: String
                            ) {
                                it?.data?.epotlyData?.let {ePotlyData->
                                    sharedPreff.getUserData()?.let {userData->
                                        viewModel.popup_message.value="Success"
                                        val dialogFragment = EPotlyReceptDialogFragment(object:
                                            CallBack {
                                            override fun getValue(s: String) {
                                                if (Objects.equals(s,"back")) {
                                                    findNavController().popBackStack()
                                                }
                                            }
                                        }, ePotlyData, userData)
                                        dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                    }

                                }


                            }

                        })
                        successPopupFragment.show(childFragmentManager, successPopupFragment.tag)*/
                    viewModel?.apply {
                        epotly_mobile.value=""
                        epotly_amt.value=""
                    }
                    viewModel?.epotlyTranspherResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel?.epotlyTranspherResponseLiveData?.value=null
                }
            }
        }
    }


}
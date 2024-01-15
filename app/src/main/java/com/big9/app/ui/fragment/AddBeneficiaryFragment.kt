package com.big9.app.ui.fragment



import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.model.BankListModel
import com.big9.app.data.model.BankListModel2


import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentAddBeneficiaryBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.activity.DashboardActivity

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.fragment.BankListBottomSheetDialog
import com.big9.app.ui.popup.CustomPopup.showDebitPopup
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.ui.popup.SuccessPopupFragment2
import com.big9.app.ui.receipt.EPotlyReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.customerId
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson
import java.util.Objects


class AddBeneficiaryFragment : BaseFragment() {
    lateinit var binding: FragmentAddBeneficiaryBinding
    private val viewModel: MyViewModel by activityViewModels()
    var bankList = ArrayList<BankListModel2>()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_beneficiary, container, false)
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
            rootView.setOnClickListener{
                activity?.let {act-> rootView.hideSoftKeyBoard(act) }
            }
            rootView3.setOnClickListener{
                activity?.let {act-> rootView.hideSoftKeyBoard(act) }
            }

            //binding.rootView.hideKeyboard()
// Set up touch listener for root layout
           /* binding.rootView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    // Check if the touch event is outside of the EditText
                    val editText = binding.etBeneficiaryBankName // Replace with your EditText
                    if (editText.isFocused) {
                        val outRect = Rect()
                        editText.getGlobalVisibleRect(outRect)
                        if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                            // Hide the keyboard
                            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(editText.windowToken, 0)
                            editText.clearFocus()
                        }
                    }
                }
                false
            }*/
            /*rootView.setOnClickListener{
                activity?.let {act->
                    binding.etBenName.hideKeyboard()

                    //binding.etBenName.clearFocus()
                }

            }*/


            btnSubmit.setOnClickListener{
                activity?.let {act->
                    if (viewModel?.beneficiaryValidation() == true){
                    //findNavController().popBackStack()

                        //
val (isLogin, loginResponse) = sharedPreff.getLoginData()
loginResponse?.let { loginData ->
    loginData.userid?.let {
        val data = mapOf(
            "userid" to loginData.userid,
            "custid" to customerId,
            "bankid" to viewModel?.bankIdBene?.value,
            "bankName" to viewModel?.beneficiary_bank_name?.value,
            "account_number" to viewModel?.beneficiary_acc?.value,
            "ifsc" to viewModel?.beneficiary_ifsc?.value,
            "benef_name" to viewModel?.beneficiary_name?.value,
        )

        val gson = Gson()
        var jsonString = gson.toJson(data)
        loginData.AuthToken?.let {
            viewModel?.addBeneficiary(it, jsonString.encrypt())
        }
    }
}
                    }
                }

            }
            tvVerify.setOnClickListener{
                if (viewModel?.beneficiaryVerifyValidation() == true){
                    showDebitPopup(tvVerify.context,object: CallBack {
                        override fun getValue(s: String) {
                            // API call
                            val (isLogin, loginResponse) = sharedPreff.getLoginData()
                            loginResponse?.let { loginData ->
                                loginData.userid?.let {
                                    val data = mapOf(
                                        "userid" to loginData.userid,
                                        "custid" to customerId,
                                        "bankid" to viewModel?.bankIdBene?.value,
                                        "bankName" to viewModel?.beneficiary_bank_name?.value,
                                        "account_number" to viewModel?.beneficiary_acc?.value,
                                        "ifsc" to viewModel?.beneficiary_ifsc?.value,
                                        "benef_name" to viewModel?.beneficiary_name?.value,
                                    )


                                    val gson = Gson()
                                    var jsonString = gson.toJson(data)
                                    loginData.AuthToken?.let {
                                        viewModel?.beneficiaryVerify(it, jsonString.encrypt())
                                    }
                                }
                            }

                        }
                    })

                }
            }

            etBeneficiaryBankName.setOnClickListener{
                activity?.let {act->
                    val bankListBottomSheetDialog = BankListBottom2SheetDialog(object : CallBack {
                        override fun getValue(s: String) {
                           // Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                        }
                    },bankList)
                    bankListBottomSheetDialog.show(
                        act.supportFragmentManager,
                        bankListBottomSheetDialog.tag
                    )
                }

            }




        }



    }

    fun initView() {
        binding.apply {

            activity?.let {act->
                        loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
            }
            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            loginResponse?.let { loginData ->
                loginData.userid?.let {
                    val data = mapOf(
                        "userid" to it,
                    )
                    val gson =  Gson();
                    var jsonString = gson.toJson(data);
                    loginData.AuthToken?.let {
                        viewModel?.addBankBankList(it,jsonString.encrypt())
                    }
                }
            }
        }
    }

    fun setObserver() {
        viewModel?.addBankBankListResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    bankList.clear()
                    it.data?.data?.let {
                        it.forEach{
                            bankList.add(BankListModel2(R.drawable.bank_name_ioc,it.name.toString(),"A/C",it.ifsc.toString(),it.id.toString()))
                        }

                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }

                else -> {}
            }
        }

        //
        viewModel?.addBeneficiaryResponseLiveData?.observe(viewLifecycleOwner) {
    when (it) {
        is ResponseState.Loading -> {
            loader?.show()
        }
        is ResponseState.Success -> {
            loader?.dismiss()
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
                    findNavController().popBackStack()
                    viewModel?.addBeneficiaryResponseLiveData?.value=null
                }

            })
            successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
        }
        is ResponseState.Error -> {
            loader?.dismiss()
            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
            viewModel?.addBeneficiaryResponseLiveData?.value=null
        }
    }
}

    viewModel?.beneficiaryVerifyResponseLiveData?.observe(viewLifecycleOwner) {
    when (it) {
        is ResponseState.Loading -> {
            loader?.show()
        }
        is ResponseState.Success -> {
            loader?.dismiss()
            it?.data?.benName?.let {
                viewModel?.beneficiary_name?.value="${it}"
            }

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
                    viewModel?.beneficiaryVerifyResponseLiveData?.value=null
                }

            })
            successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
        }
        is ResponseState.Error -> {
            loader?.dismiss()
            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
            viewModel?.beneficiaryVerifyResponseLiveData?.value=null
        }
    }
}


    }

   /* fun backPressedCheck(){
        activity?.let {act->
            act.onBackPressedDispatcher.addCallback(act, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.let {act->
                        startActivity(Intent(act, DashboardActivity::class.java).putExtra(Constants.isAfterReg,true))
                    }

                }
            })
        }

    }*/
}
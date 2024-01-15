package com.big9.app.ui.fragment


import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import checkUserModel
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentMoneyTranspherBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants.bankCode
import com.big9.app.utils.helpers.Constants.customerId
import com.big9.app.utils.helpers.Constants.totalBankLimit
import com.google.gson.Gson

class MoneyTranspherFragment : BaseFragment() {
    lateinit var binding: FragmentMoneyTranspherBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    var isNewUser=false
    var isNewCall=true
    var isBankError=false
    var remiterUserData: checkUserModel? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_money_transpher, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel?.sendMoneyVisibility?.value=false
        viewModel?.mobileSendMoney?.value=""
        viewModel?.nameSendMoney?.value=""
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {
        binding.apply {
            rootView.setOnClickListener{
                activity?.let {act-> rootView.hideSoftKeyBoard(act) }
            }

            imgBack.back()

            activity?.let {act->
                btnSubmit.setOnClickListener{
                    if (viewModel?.MoneyTranspherValidation() == true) {
                        if(viewModel?.sendMoneyVisibility?.value==true ){
                            if (isNewUser){

                                    remiterUserData.apply {
                                        val bundle = Bundle()

                                        bundle.putString("customerid", customerId)
                                            bundle.putString("customer_number", viewModel?.mobileSendMoney?.value)
                                            bundle.putString("customer_name", viewModel?.nameSendMoney?.value)
                                            findNavController().navigate(R.id.action_moneyTranspherFragment_to_otpNewRmnFragment,bundle)


                                    }


                               // isNewUser=false

                            }
                            else {

                                if(!isBankError){
                                    findNavController().navigate(R.id.action_moneyTranspherFragment_to_beneficiaryFragment)
                                }

                            }
                          //  isNewUser=false
                        }
                        else {
                            val (isLogin, loginResponse) = sharedPreff.getLoginData()
                            if (isLogin) {
                                loginResponse?.let { loginData ->
                                    viewModel?.apply {

                                        val data = mapOf(
                                            "customer_number" to viewModel?.mobileSendMoney?.value,
                                            "userid" to loginData.userid,
                                            "customer_name" to viewModel?.nameSendMoney?.value,

                                            )

                                        val gson = Gson()
                                        var jsonString = gson.toJson(data)
                                        loginData.AuthToken?.let {
                                            val data = jsonString.encrypt()
                                            checkUser(it, data)
                                        }
                                    }

                                }
                            }
                        }





                        //findNavController().navigate(R.id.action_moneyTranspherFragment_to_beneficiaryFragment)
                        /*activity?.let {act->
                            val selectTransactionTypeBottomSheetDialog = SelectTransactionTypeBottomSheetDialog(object : CallBack {
                                override fun getValue(s: String) {

                                    val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                                        override fun getValue(s: String) {
                                            Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                    tpinBottomSheetDialog.show(
                                        act.supportFragmentManager,
                                        tpinBottomSheetDialog.tag
                                    )


                                }
                            })
                            selectTransactionTypeBottomSheetDialog.show(
                                act.supportFragmentManager,
                                selectTransactionTypeBottomSheetDialog.tag
                            )
                        }

*/

                    }
                }

            }
            btnSubmit2.setOnClickListener{
                callNewUser()
            }

        }
    }
    fun initView() {

        activity?.let {act->
         loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        binding.apply {

        }

    }

    fun setObserver() {
        binding?.apply {
            viewModel?.mobileSendMoney?.observe(viewLifecycleOwner){
              //  viewModel?.sendMoneyVisibility?.value = it.length==10
                // need to check from api
            }
        }


        // 
        viewModel?.checkUserResponseLiveData?.observe(viewLifecycleOwner) {
        when (it) {
        is ResponseState.Loading -> {
            loader?.show()

        }
        is ResponseState.Success -> {
            loader?.dismiss()
            remiterUserData=it.data

            remiterUserData?.response?.let {
                var bank1=0
                var bank2=0
                var bank3=0


                it.bank1Limit?.let {
                    bank1=it
                }
                it.bank2Limit?.let {
                    bank2=it
                }
                it.bank3Limit?.let {
                    bank3=it
                }


                try {
                   /* var bank1_=bank1.plus(bank2)
                    var bank2_=bank1_.plus(bank3)*/
                    var total=bank1+bank2+bank3
                    totalBankLimit=total.toString()
                }catch (e:Exception){}

               /* bank1

                "bank3_limit":25000,"bank3_status":"yes","bank2_limit":25000,"bank1_limit":25000}}*/
            }
            customerId=remiterUserData?.custID.toString()
            remiterUserData?.status?.let {status->

              if (status=="200"){
                  isNewUser=false
                  viewModel?.mobileSendMoney?.value=it.data?.response?.mobile.toString()
                  viewModel?.nameSendMoney?.value="${it.data?.response?.fname.toString()} ${it.data?.response?.lname.toString()}".replace("null","")
                  isBankError=false

                  isNewCall=false
                  binding.btnSubmit.visibility=View.VISIBLE
                  binding.btnSubmit2.visibility=View.GONE
                  /* val bundle2 = Bundle()
                        var  castIdData= remiterUserData?.custID.toString()
                       bundle2.putString("customerid", castIdData)*/
                  Handler(Looper.getMainLooper()).postDelayed({
                      binding.btnSubmit.visibility=View.VISIBLE
                      binding.btnSubmit2.visibility=View.GONE
                  },100)
                  findNavController().navigate(R.id.action_moneyTranspherFragment_to_beneficiaryFragment)
              }
               else   if (status.lowercase()=="201"){
                  isNewUser=false
                  //isNewCall=true
                  viewModel?.sendMoneyVisibility?.value = true
                  binding.etMob.isFocusable = false
                  binding.etMob.isFocusableInTouchMode = false
                  Handler(Looper.getMainLooper()).postDelayed({
                      binding.btnSubmit.visibility=View.GONE
                      binding.btnSubmit2.visibility=View.VISIBLE
                  },100)
                  //callNewUser()


                   /*if (remiterUserData?.stateresp==null){
                       isNewUser=false
                       isBankError=true
                       isNewCall=false
                       Toast.makeText(requireContext(), "Unable to send OTP", Toast.LENGTH_SHORT).show()
                   }
                  else {

                   }*/
              }
               else  if (status=="202"){
                  isBankError=false
                  isNewUser=true
                  isNewCall=false
                  bankCode= remiterUserData?.stateresp.toString()
                  viewModel?.sendMoneyVisibility?.value = true
                  binding.etMob.isFocusable = false
                  binding.etMob.isFocusableInTouchMode = false
                  Handler(Looper.getMainLooper()).postDelayed({
                      binding.btnSubmit.visibility=View.VISIBLE
                      binding.btnSubmit2.visibility=View.GONE
                  },100)

              } else {

              }
                /*   isNewUser=true
                   viewModel?.sendMoneyVisibility?.value = true
                   binding.etMob.isFocusable = false
                   binding.etMob.isFocusableInTouchMode = false*/
           }
            viewModel?.checkUserResponseLiveData?.value=null
        }
        is ResponseState.Error -> {
            loader?.dismiss()
            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
            viewModel?.checkUserResponseLiveData?.value=null
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnSubmit.visibility=View.VISIBLE
                binding.btnSubmit2.visibility=View.GONE
            },100)
        }

            else -> {


            }
        }
}
        viewModel?.checkUserResponseLiveData2?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()

                }
                is ResponseState.Success -> {
                    loader?.dismiss()
                    remiterUserData=it.data

                    remiterUserData?.response?.let {
                        var bank1=0
                        var bank2=0
                        var bank3=0


                        it.bank1Limit?.let {
                            bank1=it
                        }
                        it.bank2Limit?.let {
                            bank2=it
                        }
                        it.bank3Limit?.let {
                            bank3=it
                        }


                        try {
                            /* var bank1_=bank1.plus(bank2)
                             var bank2_=bank1_.plus(bank3)*/
                            var total=bank1+bank2+bank3
                            totalBankLimit=total.toString()
                        }catch (e:Exception){}

                        /* bank1

                         "bank3_limit":25000,"bank3_status":"yes","bank2_limit":25000,"bank1_limit":25000}}*/
                    }
                    customerId=remiterUserData?.custID.toString()
                    remiterUserData?.status?.let {status->

                        if (status=="200"){
                            isNewUser=false
                            viewModel?.mobileSendMoney?.value=it.data?.response?.mobile.toString()
                            viewModel?.nameSendMoney?.value="${it.data?.response?.fname.toString()} ${it.data?.response?.lname.toString()}".replace("null","")
                            isBankError=false

                            isNewCall=false

                            /* val bundle2 = Bundle()
                                  var  castIdData= remiterUserData?.custID.toString()
                                 bundle2.putString("customerid", castIdData)*/

                            findNavController().navigate(R.id.action_moneyTranspherFragment_to_beneficiaryFragment)
                        }
                        else   if (status.lowercase()=="201"){
                            isNewUser=false
                            //isNewCall=true
                            viewModel?.sendMoneyVisibility?.value = true
                            binding.etMob.isFocusable = false
                            binding.etMob.isFocusableInTouchMode = false

                            //callNewUser()


                            /*if (remiterUserData?.stateresp==null){
                                isNewUser=false
                                isBankError=true
                                isNewCall=false
                                Toast.makeText(requireContext(), "Unable to send OTP", Toast.LENGTH_SHORT).show()
                            }
                           else {

                            }*/
                        }
                        else  if (status=="202"){
                            isBankError=false
                            isNewUser=true
                            isNewCall=false
                            bankCode= remiterUserData?.stateresp.toString()
                            viewModel?.sendMoneyVisibility?.value = true
                            binding.etMob.isFocusable = false
                            binding.etMob.isFocusableInTouchMode = false



                            remiterUserData.apply {
                                val bundle = Bundle()

                                bundle.putString("customerid", customerId)
                                bundle.putString("customer_number", viewModel?.mobileSendMoney?.value)
                                bundle.putString("customer_name", viewModel?.nameSendMoney?.value)
                                findNavController().navigate(R.id.action_moneyTranspherFragment_to_otpNewRmnFragment,bundle)


                            }
                        }
                        /*   isNewUser=true
                           viewModel?.sendMoneyVisibility?.value = true
                           binding.etMob.isFocusable = false
                           binding.etMob.isFocusableInTouchMode = false*/
                    }
                    viewModel?.checkUserResponseLiveData2?.value=null
                }
                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel?.checkUserResponseLiveData2?.value=null

                }

                else -> {}
            }
        }
    }

    private fun callNewUser() {
        if (viewModel.MoneyTranspherValidation2()) {
            val (isLogin, loginResponse) = sharedPreff.getLoginData()
            if (isLogin) {
                loginResponse?.let { loginData ->
                    viewModel?.apply {

                        val data = mapOf(
                            "customer_number" to viewModel?.mobileSendMoney?.value,
                            "userid" to loginData.userid,
                            "customer_name" to viewModel?.nameSendMoney?.value,
                            )

                        val gson = Gson()
                        var jsonString = gson.toJson(data)
                        loginData.AuthToken?.let {
                            val data = jsonString.encrypt()
                            checkUser2(it, data)
                        }
                    }

                }
            }
        }
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager = binding.root.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.apply {
            root.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            etMob?.let { focusedView ->
                inputMethodManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
            }
        }
    }

}
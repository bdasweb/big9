package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.adapter.AccountDetailsAdapter
import com.big9.app.data.model.AccountDetailsModel
import com.big9.app.data.model.MoveToBankBankListData
import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentMoveToBankBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.ui.receipt.MoveToBankReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack2
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson
import java.util.Objects


class MoveToBankFragment : BaseFragment() {
    lateinit var binding: FragmentMoveToBankBinding
    private val viewModel: MyViewModel by activityViewModels()
    var authViewModel: AuthViewModel?=null
    var accountDetailsList = ArrayList<AccountDetailsModel>()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_move_to_bank, container, false)
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
            fabAddBank.setOnClickListener{
                viewModel?.apply {
                    beneficiary_bank_name?.value=""
                    beneficiary_ifsc?.value=""
                    beneficiary_acc?.value=""
                    beneficiary_name?.value=""
                }

                authViewModel?.filePath?.value=null
                findNavController().navigate(R.id.action_moveToBankFragment_to_addBankFragment)
            }
          }
         }
    fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
            getBankList()
        }
    }
    private fun getBankList(){
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        if (isLogin){
            loginResponse?.let { loginData->
                viewModel?.apply {
                    val  data = mapOf(
                        "userid" to loginData.userid,
                    )
                    val gson= Gson()
                    var jsonString = gson.toJson(data)
                    loginData.AuthToken?.let {
                        moveToBank(it,jsonString.encrypt())
                    }
                    }
            }
        }
    }
    fun setObserver() {
        viewModel.apply {
            moveToBankReceptLiveData?.observe(viewLifecycleOwner){
                when (it) {
                    is ResponseState.Loading -> {
                        loader?.show()
                    }
                    is ResponseState.Success -> {
                        loader?.dismiss()
                        setRecycleView(it?.data?.data)
                        moveToBankReceptLiveData?.value=null
                    }
                    is ResponseState.Error -> {
                        loader?.dismiss()
                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        moveToBankReceptLiveData?.value=null
                    }
                }
            }
            submit_moveToBankReceptLiveData?.observe(viewLifecycleOwner){
                when (it) {
                    is ResponseState.Loading -> {
                        loader?.show()
                    }
                    is ResponseState.Success -> {
                        loader?.dismiss()
                        popup_message.value=it?.data?.Description
                        val successPopupFragment = SuccessPopupFragment(
                            object :
                                CallBack4 {
                                override fun getValue4(
                                    s1: String,
                                    s2: String,
                                    s3: String,
                                    s4: String
                                ) {
                                    popup_message.value="Success"
                                    findNavController().popBackStack()
                                    /*val dialogFragment = MoveToBankReceptDialogFragment(object:
                                        CallBack {
                                        override fun getValue(s: String) {
                                            if (Objects.equals(s,"back")) {
                                                findNavController().popBackStack()
                                            }
                                        }
                                        })
                                    dialogFragment.show(childFragmentManager, dialogFragment.tag)*/
                                  }
                            }
                        )
                        successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
                        submit_moveToBankReceptLiveData?.value=null
                    }

                    is ResponseState.Error -> {
                        loader?.dismiss()
                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        submit_moveToBankReceptLiveData?.value=null
                    }
                }
            }
        }
    }

    private fun setRecycleView(data: ArrayList<MoveToBankBankListData>?) {
        binding.recycleViewBankDetails.apply {
            accountDetailsList.clear()
            data?.let {bankList->
                bankList.forEach {data->
                    var approvalStatus=false
                    if (data.delflag.toString()=="0"){
                        approvalStatus=true
                    }
                    accountDetailsList.add(AccountDetailsModel(data.name.toString(),approvalStatus,data.acno.toString(),data.ifsc.toString(),data.delflagStatus.toString(),data.id.toString()))
                }
            }
            binding.fabAddBank.visibility=View.GONE
            data?.forEach { item ->
                if (item.delflag != "0") {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.fabAddBank.visibility=View.VISIBLE
                    },1000)

                    return@forEach
                }

            }

              //accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
             // accountDetailsList.add(AccountDetailsModel("Test User",false,"0087200100008770","PUNB00389600"))
            /*accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))*/
            adapter= AccountDetailsAdapter(accountDetailsList,object: CallBack2 {
                override fun getValue2(s: String,bankId: String) {
                    activity?.let {act->
                        val selectTransactionTypeBottomSheetDialog = SelectTransactionTypeBottomSheetDialog(object :
                            CallBack {
                            override fun getValue(amount: String) {


                                val tpinBottomSheetDialog = TpinBottomSheetDialog(object :
                                    CallBack {
                                    override fun getValue(tpin: String) {

                                        val (isLogin, loginResponse) =sharedPreff.getLoginData()
                                        if (isLogin){
                                            loginResponse?.let {loginData->
                                                viewModel?.apply {
                                                    var tType=""
                                                    if (viewModel?.IMPSIsActive?.value==true){
                                                        tType="IMPS"
                                                    }
                                                    else if(viewModel?.NEFTIsActive?.value==true){
                                                        tType="NEFT"
                                                    }

                                                    val  data = mapOf(
                                                        "userid" to loginData.userid,
                                                        "tpin" to tpin,
                                                        "type" to tType,

                                                        "amount" to amount,
                                                        "id" to bankId,

                                                    )





                                                    val gson= Gson()
                                                    var jsonString = gson.toJson(data)
                                                    loginData.AuthToken?.let {
                                                        submitMovetobank(it,jsonString.encrypt())
                                                    }
                                                }

                                            }
                                        }
                                    }
                                })
                                activity?.let {act->
                                    tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)
                                }
                                /*viewModel.receiveStatus.value="Failed"
                                recycleViewReceiptList.clear()
                                recycleViewReceiptList.add(ReceiptModel("Transaction Id","300000025", type = 4))
                                recycleViewReceiptList.add(ReceiptModel("Subscriber/ Customer Number","8583863153", type = 1))
                                recycleViewReceiptList.add(ReceiptModel("Transaction Amount","₹10.00", type = 2))
                                recycleViewReceiptList.add(ReceiptModel("Running Balance","₹200.22", type = 3))
                                recycleViewReceiptList.add(ReceiptModel("Operator","AIRTEL", type = 4))
                                recycleViewReceiptList.add(ReceiptModel("Operator ID","N/A", type = 1))*/
                                //ReceiptModel(val property:String, val reportValue:String, val title:String="", val price:String="", val transactionMessage:String="", val transactionId:String="", val userName:String="", val rrnId:String="", val type:Int=1)
                                //val dialogFragment = ReceptDialogFragment()
                                //dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                //Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                            }
                        })
                        selectTransactionTypeBottomSheetDialog.show(
                            act.supportFragmentManager,
                            selectTransactionTypeBottomSheetDialog.tag
                        )
                    }

                }

            })
        }
    }


}
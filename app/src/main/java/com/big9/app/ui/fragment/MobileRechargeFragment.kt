package com.big9.app.ui.fragment


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.model.PrepaidMoboleTranspherModel
import com.big9.app.data.model.ReceiptModel
import com.big9.app.data.model.allReport.PostPaidMobileTranspherModel
import com.big9.app.data.viewMovel.MobileRechargeViewModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentMobileRechargeBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.receipt.newRecept.PostPaidnewMobileReceptDialogFragment
import com.big9.app.ui.receipt.newRecept.PrePaidMobileReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.common.MethodClass.getLocalIPAddress
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.isDthOperator
import com.big9.app.utils.helpers.Constants.isFirstPageOpeenPostPaidMobile
import com.big9.app.utils.`interface`.CallBack


class MobileRechargeFragment : BaseFragment() {
    lateinit var binding: FragmentMobileRechargeBinding
    private val viewModel: MyViewModel by activityViewModels()
    //private val mobileRechargeViewModel: MobileRechargeViewModel?=null
    private val mobileRechargeViewModel: MobileRechargeViewModel by viewModels()
    private var loader: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_recharge, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        mobileRechargeViewModel?.prePaidMobileTranspherResponseLiveData?.value=null
        setObserver()
        onViewClick()
    }


    private fun onViewClick() {
        binding.apply {
            rootView.setOnClickListener {
                activity?.let { act -> rootView.hideSoftKeyBoard(act) }
            }
            //imgBack.back()
            imgBack.setOnClickListener{
                findNavController().navigate(R.id.action_mobileRechargeFragment_to_homeFragment2)
            }
            viewModel?.prepaitIsActive?.value=true
            llPostPaid.setOnClickListener{
                viewModel?.prepaitIsActive?.value=false
                viewModel?.postpaitIsActive?.value=true
            }

            llPrePaid.setOnClickListener{
                viewModel?.prepaitIsActive?.value=true
                viewModel?.postpaitIsActive?.value=false
            }
            tvOperator.setOnClickListener{
                /*val operatorDialog = OperatorFragment(object : CallBack {
                    override fun getValue(s: String) {
                       viewModel?.operator?.value=s
                    }
                })*/
                activity?.let {act->
                    isDthOperator =false
                    findNavController().navigate(R.id.action_mobileRechargeFragment_to_operatorFragment)
                    //operatorDialog.show(act.supportFragmentManager, operatorDialog.tag)
                }

            }
            btnBrowse.setOnClickListener{
                /*val browserPlainDialog = BrowserPlainFragment(object : CallBack {
                    override fun getValue(s: String) {
                       viewModel?.operator?.value=s
                    }
                })*/
                if (viewModel?.mobileValidation() == true){
                    activity?.let { act ->
                        findNavController().navigate(R.id.action_mobileRechargeFragment_to_browserPlainFragment)
                        //browserPlainDialog.show(act.supportFragmentManager, browserPlainDialog.tag)
                    }
                }
            }

            btnSubmit.setOnClickListener{
                activity?.let {act->
                    if (viewModel?.regValidation() == true){
                        //btnSubmit.setBottonLoader(false,llSubmitLoader)
                            val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                                override fun getValue(s: String) {
                                    submit(s)

                                }
                            })
                            //tpinBottomSheetDialog.isCancelable=false
                            tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)

                    }
                }

            }

        }



    }

    private fun submit(tpin: String) {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

                val (isLogin, loginResponse) =sharedPreff.getLoginData()
                if (isLogin){
                    loginResponse?.let {loginData->
                        val data ="{\"userid\":\"${loginData.userid}\",\"operator\":\"${viewModel.operator.value}\",\"opcode\":\"${viewModel.operatorCode.value}\",\"tpin\":\"${tpin}\",\"custno\":\"${viewModel.mobile.value}\",\"rcamt\":\"${viewModel.amt.value}\",\"IP\":\"${getLocalIPAddress()}\"}"
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
                                if (viewModel?.prepaitOrPostPaid?.value== Constants.Postpaid) {
                                    mobileRechargeViewModel?.PostPaidMobileTranspher(it, data.encrypt())
                                }
                                else if (viewModel?.prepaitOrPostPaid?.value== Constants.Prepaid) {

                                    mobileRechargeViewModel?.PrePaidMobileTranspher(it, data.encrypt())
                                }
                                //loader?.show()
                            }

                    }
                }

        }
    }

    fun initView() {
        backPressedCheck()
        binding.apply {
            activity?.let {
                loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
            }
            rootView.setOnClickListener {
                activity?.let { act -> rootView.hideSoftKeyBoard(act) }
            }
            etAmt.setupAmount()
            if (viewModel?.prepaitOrPostPaid?.value== Constants.Postpaid) {
                binding.btnBrowse.visibility=View.GONE
            }
            else if (viewModel?.prepaitOrPostPaid?.value== Constants.Prepaid) {
                binding.btnBrowse.visibility=View.VISIBLE
            }
            if (isFirstPageOpeenPostPaidMobile) {
                isFirstPageOpeenPostPaidMobile = false
                activity?.let { act ->
                    isDthOperator = false
                    findNavController().navigate(R.id.action_mobileRechargeFragment_to_operatorFragment)
                    //operatorDialog.show(act.supportFragmentManager, operatorDialog.tag)
                }
            }
            //viewModel?.minMobileLength.value

        }
    }

    fun setObserver() {

        mobileRechargeViewModel?.postPaidMobileTranspherResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()

                    //val data= PostPaidMobileTranspherModel()
                    var dateTime=it?.data?.timestamp

                    it?.data?.data?.let {
                        if (it?.size!! > 0 ?: 0) {
                           it?.get(0)?.let {
                                /* data?.amount=it.amount
                                 data?.mobileno=it.mobileno
                                 data?.curramt=it.curramt
                                 data?.refillid=it.refillid
                                 data?.status=it.status
                                 data.image=viewModel?.selectrdOperator?.value*/


                               viewModel.mobile.value = ""
                               viewModel.operator.value = ""
                               viewModel.amt.value = ""

                                Constants.recycleViewReceiptList.clear()
                                viewModel?.receiveStatus?.value=getString(R.string.mobile_recharged)
                                Constants.recycleViewReceiptList.add(ReceiptModel("Refill Id",it.refillid.toString().replace("null","")))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Operator",it.operator.toString().replace("null","")))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Operator Id",it.operatorid.toString().replace("null","")))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Mobile No",it.mobileno.toString().replace("null","")))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Amount",it.amount.toString().replace("null","")))

                                Constants.recycleViewReceiptList.add(ReceiptModel("Date and Time",dateTime.toString().replace("null","")))


                                Constants.recycleViewReceiptList.add(ReceiptModel("Status",it.status.toString().replace("null","")))
                                val dialogFragment = PostPaidnewMobileReceptDialogFragment()
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)
                            }
                        }
                    }

                   /* val dialogFragment = PostPaidMobileReceptDialogFragment(object: CallBack {
                                    override fun getValue(s: String) {
                                        if (Objects.equals(s,"back")) {
                                            findNavController().popBackStack()
                                        }
                                    }
                                }, data)
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)*/
                    mobileRechargeViewModel?.postPaidMobileTranspherResponseLiveData?.value=null

                }

                is ResponseState.Error -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    mobileRechargeViewModel?.postPaidMobileTranspherResponseLiveData?.value=null
                }
            }
        }
        mobileRechargeViewModel?.prePaidMobileTranspherResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()

                    //    val data= PrepaidMoboleTranspherModel()
                    var dateTime=it?.data?.timestamp
                    it?.data?.data?.let {
                        if (it?.size!! > 0 ?: 0) {
                            it?.get(0)?.let {
                                /* data?.amount=it.amount
                                 data?.mobileno=it.mobileno
                                 data?.curramt="${it.curramt}"
                                 data?.refillid=it.refillid
                                 data?.status=it.status
                                 data.image=viewModel?.selectrdOperator?.value*/


                                viewModel.mobile.value = ""
                                viewModel.operator.value = ""
                                viewModel.amt.value = ""


                                Constants.recycleViewReceiptList.clear()
                                viewModel?.receiveStatus?.value=getString(R.string.mobile_recharged)
                                Constants.recycleViewReceiptList.add(ReceiptModel("Transaction Id",it.refillid.toString().replace("null","")))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Operator",it.operator.toString().replace("null","")))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Operator Id",it.operatorid.toString().replace("null","")))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Mobile No",it.mobileno.toString().replace("null","")))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Amount",it.amount.toString().replace("null","")))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Date and Time",dateTime.toString().replace("null","")))
                                //Constants.recycleViewReceiptList.add(ReceiptModel("Refill Id",it.refillid.toString()))
                                Constants.recycleViewReceiptList.add(ReceiptModel("Status",it.status.toString().replace("null","")))
                                val dialogFragment = PrePaidMobileReceptDialogFragment()
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)


                            }
                        }
                    }


                       /* val dialogFragment = MobileReceptDialogFragment(object: CallBack {
                            override fun getValue(s: String) {
                                if (Objects.equals(s,"back")) {
                                    findNavController().popBackStack()
                                }
                            }
                        }, data)
                        dialogFragment.show(childFragmentManager, dialogFragment.tag)*/


                    mobileRechargeViewModel?.prePaidMobileTranspherResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()

                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    mobileRechargeViewModel?.prePaidMobileTranspherResponseLiveData?.value=null

                }
            }
        }
    }

    fun backPressedCheck(){
        activity?.let {act->
            act.onBackPressedDispatcher.addCallback(act, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.let {act->
                        val intent = Intent(act, DashboardActivity::class.java).apply {
                            putExtra(Constants.isAfterReg, true)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        act.startActivity(intent)
                    }
                }
            })
        }

    }
}
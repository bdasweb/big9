package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.adapter.PhonePadAdapter
import com.big9.app.data.model.ReceiptModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentTransactionOtpBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.ui.popup.SuccessPopupFragment2
import com.big9.app.ui.receipt.newRecept.CreditCardReceptDialogFragment
import com.big9.app.ui.receipt.newRecept.PostPaidnewMobileReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.CallBack4
import com.big9.app.utils.`interface`.KeyPadOnClickListner
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class TransactionOtpFragment : BaseFragment() {
    lateinit var binding: FragmentTransactionOtpBinding
    private val viewModel: MyViewModel by activityViewModels()
    var keyPad = ArrayList<Int>()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_otp, container, false)
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
            /*imgBack.setOnClickListener{
                findNavController().navigate(R.id.homeFragment)
            }*/
           /* imgLogo.setOnClickListener{
                findNavController().popBackStack()
            }*/

            /*tdResendOtp.setOnClickListener {
                    if (tdResendOtp.text.toString()==getString(R.string.resend_otp_title)){
                        //otp api call
                        cownDown()
                    }
            }*/



            btnSubmit.setOnClickListener{

                if(viewModel?.otp?.value?.length==6){
                    verifyOTP()
                }

            }

        }



    }

    fun verifyOTP(){
        activity?.let {act->

                val (isLogin, loginResponse) =sharedPreff.getLoginData()

                    loginResponse?.let { loginData ->
                        viewModel?.apply {
                            val  data = mapOf(
                                "userid" to loginData.userid,
                                "cardholdername" to credit_holder_name.value,
                                "network" to select_card_type.value,
                                "cardnumber" to credit_card.value,
                                "mobile" to credit_mobile.value,
                                "amount" to credit_amt.value,
                                "remarks" to credit_remarks.value,
                                "otp" to otp?.value,
                                "ccpay_transid" to creditCardID.value,
                                "stateresp" to stateresp.value


                            )

                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {

                                creditCardVeryfyOTP(it,jsonString.encrypt())
                            }
                        }
                    }



        }
    }

    fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
        binding.apply {
            viewModel?.apply {
                otpMobile.value=credit_mobile.value
            }

        }


            cownDown()
            keyPad.clear()
            keyPad.add(1)
            keyPad.add(2)
            keyPad.add(3)
            keyPad.add(4)
            keyPad.add(5)
            keyPad.add(6)
            keyPad.add(7)
            keyPad.add(8)
            keyPad.add(9)
            keyPad.add(10)
            keyPad.add(0)
            keyPad.add(11)
            binding.recyclePhonePad.apply {
                //authViewModel.keyPadValue.value=getString(R.string.mobile_no_hint)
                adapter= PhonePadAdapter(keyPad,object : KeyPadOnClickListner {
                    override fun onClick(item: Int) {

                        viewModel?.otp?.value?.apply {
                            if (item<=9 ) {
                                if (this.length!=6) {

                                    // binding.firstPinView.text=this
                                    viewModel.otp.value = "${this}${item}"
                                    /*if(viewModel?.otp?.value?.length==6)
                                    {
                                        verifyOTP()
                                    }*/

                                    //binding.firstPinView.setText(authViewModel.otp.value)
                                }
                            }
                            else if(item==10){
                                //authViewModel.keyPadValue.value =""
                            }
                            else {
                                if (this.isNotEmpty()) {
                                    viewModel.otp.value = this.toString().substring(0, this.length - 1)
                                    //binding.firstPinView.setText(authViewModel.otp.value)

                                }

                            }
                        }

                    }

                })
                isNestedScrollingEnabled=false
            }



    }

    fun setObserver() {
        viewModel?.creditCardVeryfyOTPResponseLiveData?.observe(viewLifecycleOwner){
            when(it){
                is ResponseState.Loading -> {
                    loader?.show()
                }
                is ResponseState.Success -> {
                    loader?.dismiss()
                    viewModel?.apply {


                        it?.data?.data?.let {data->
                            if (data.size>0){
                                data?.get(0)?.let {
                                    /*viewModel?.popup_message?.value=it?.status.toString().replace("null","")
                                    val successPopupFragment = SuccessPopupFragment2(object :
                                        CallBack4 {
                                        override fun getValue4(
                                            s1: String,
                                            s2: String,
                                            s3: String,
                                            s4: String
                                        ) {*/
                                            var cCard:String=credit_card.value?.maskExceptLastFour().toString()
                                            viewModel.popup_message.value="Success"
                                            Constants.recycleViewReceiptList.clear()
                                            // viewModel?.receiveStatus?.value="getString(R.string.mobile_recharged)"
                                            Constants.recycleViewReceiptList.add(ReceiptModel("Transaction Id",it?.refId.toString().replace("null","")))
                                            Constants.recycleViewReceiptList.add(ReceiptModel("Transaction Date",it?.transDate.toString().replace("null","")))
                                            Constants.recycleViewReceiptList.add(ReceiptModel("Card Number: ",cCard))
                                            Constants.recycleViewReceiptList.add(ReceiptModel("Card Type: ",it.network.toString().replace("null","")))
                                            //Constants.recycleViewReceiptList.add(ReceiptModel("Current Balance : ",it?.usercurrbal.toString()))
                                            Constants.recycleViewReceiptList.add(ReceiptModel("Amount",it.amount.toString().replace("null","")))
                                            Constants.recycleViewReceiptList.add(ReceiptModel("UTR",it.utr.toString().replace("null","")))
                                            Constants.recycleViewReceiptList.add(ReceiptModel("Date and Time",MethodClass.getCurrentDateTime()))

                                            Constants.recycleViewReceiptList.add(ReceiptModel("Status",it.status.toString().replace("null","")))

                                            val dialogFragment = CreditCardReceptDialogFragment()
                                            dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                            credit_card.value=""
                                            credit_holder_name.value=""
                                            credit_mobile.value=""
                                            credit_mobile.value=""
                                            credit_amt.value=""
                                            credit_remarks.value=""
                                            otp.value=""
                                            // findNavController().popBackStack(R.id.homeFragment2,false)
                                            //findNavController().popBackStack()
                                      /*  }

                                    })
                                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)*/


                                }
                            }
                        }



                    }

                    viewModel?.creditCardVeryfyOTPResponseLiveData?.value=null
                }
                is ResponseState.Error -> {
                    loader?.dismiss()
                    viewModel?.apply {
                       /* credit_card.value=""
                        credit_holder_name.value=""
                        credit_mobile.value=""
                        credit_mobile.value=""
                        credit_amt.value=""
                        credit_remarks.value=""*/
                    }
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel?.creditCardVeryfyOTPResponseLiveData?.value=null
                }
            }
        }
       /* viewModel?.creditCardVeryfyOTPResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    val successPopupFragment = SuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            // findNavController().popBackStack(R.id.homeFragment2,false)
                            //findNavController().popBackStack()


                        }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)


                    }
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }*/
        }


    fun cownDown() {


        val totalTimeInMillis: Long = TimeUnit.SECONDS.toMillis(120) // 30 seconds countdown

        var countDownTimer= object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                //authViewModel.timingValue.value = getString(R.string.resend_otp,secondsRemaining)/*"Resend OTP after <font color='#B80A13'>$secondsRemaining</font> second"*/
                //binding.tdResendOtp.text = getString(R.string.resend_otp,secondsRemaining)/*"Resend OTP after <font color='#B80A13'>$secondsRemaining</font> second"*/
                setTimerValue(secondsRemaining)
            }

            override fun onFinish() {
                val spannableString = SpannableStringBuilder()
                //authViewModel.timingValue.value =  "Resend OTP"
                //binding.tdResendOtp.text =  "Resend OTP"
                val firstPart = getString(R.string.resend_otp_title)
                spannableString.append(firstPart)
                spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.logo_color)),
                    spannableString.length - firstPart.length,
                    spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.tdResendOtp.text =spannableString
            }
        }


        countDownTimer.start()

    }
    private fun setTimerValue(secondsRemaining: Long) {
        // Create a SpannableStringBuilder
        val spannableString = SpannableStringBuilder()

        // Add the first part of the text with one color
        val firstPart = "Resend OTP after "
        spannableString.append(firstPart)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
            spannableString.length - firstPart.length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Add the second part of the text with a different color
        val secondPart = "$secondsRemaining"
        spannableString.append(secondPart)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.pink2)),
            spannableString.length - secondPart.length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.append("s")
        // Set the SpannableStringBuilder as the text for the TextView
        binding.tdResendOtp.text = spannableString
    }

    fun convertMillisToHMS(milliseconds: Long): String {
        val hours = milliseconds / 3600000
        val remainingMinutes = (milliseconds % 3600000) / 60000
        val remainingSeconds = (milliseconds % 60000) / 1000

        return String.format("%02d:%02d:%02d", hours, remainingMinutes, remainingSeconds)
    }

    fun String.maskExceptLastFour(): String {
        val visibleLength = this.length - 4
        return "*".repeat(visibleLength) + this.substring(visibleLength)
    }
}
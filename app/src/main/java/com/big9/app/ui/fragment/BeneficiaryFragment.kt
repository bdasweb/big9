package com.big9.app.ui.fragment


import BeneficiaryListAdapter2
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import beneficiaryListData
import com.big9.app.R
import com.big9.app.data.model.BeneficiaryListModel

import com.big9.app.data.model.BeneficiaryListModel2
import com.big9.app.data.model.ReceiptModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentBeneficiaryBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.SuccessPopupFragment2
import com.big9.app.utils.*
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.totalBankLimit
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.big9.app.utils.`interface`.CallBack7
import com.google.gson.Gson

import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter


class BeneficiaryFragment : BaseFragment() {
    lateinit var binding: FragmentBeneficiaryBinding
    private val viewModel: MyViewModel by activityViewModels()
    var beneficiaryList = ArrayList<BeneficiaryListModel2>()
    var beneficiaryListAdapter :BeneficiaryListAdapter2?=null
    private var loader: Dialog? = null
    var customerid=""
    var bankName=""
    var bankAccount=""
    var benName=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beneficiary, container, false)
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
            tvAddBeneficiary.setOnClickListener {
                viewModel?.apply {
                    beneficiary_bank_name?.value=""
                    beneficiary_ifsc?.value=""
                    beneficiary_acc?.value=""
                    beneficiary_name?.value=""
                }

                findNavController().navigate(R.id.action_beneficiaryFragment_to_addBeneficiaryFragment)
            }
          }

        }


    fun initView() {

   activity?.let {act->
               loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
       /*customerid = arguments?.getString("customerid").toString()*/

   }
val (isLogin, loginResponse) = sharedPreff.getLoginData()
loginResponse?.let { loginData ->
    loginData.userid?.let {
        val data = mapOf(
            "userid" to loginData.userid,
            "custid" to Constants.customerId
        )
        val gson = Gson()
        var jsonString = gson.toJson(data)
        loginData.AuthToken?.let {
            viewModel?.beneficiaryList(it, jsonString.encrypt())
        }
    }
}


    }

    fun setObserver() {
        binding.apply {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.totalLimit.text="Combined Limit: ₹$totalBankLimit"
            },100)

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    beneficiaryListAdapter?.filter?.filter(s)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }


        //
        viewModel?.beneficiaryListResponseLiveData?.observe(viewLifecycleOwner) {
        when (it) {
        is ResponseState.Loading -> {
            loader?.show()
        }
        is ResponseState.Success -> {
            loader?.dismiss()
            setRecycleView(it?.data?.data)
            viewModel?.beneficiaryListResponseLiveData?.value=null
        }
        is ResponseState.Error -> {
            loader?.dismiss()

            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
            viewModel?.beneficiaryListResponseLiveData?.value=null
        }
    }
}



        viewModel?.moneyTransferResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }
                is ResponseState.Success -> {
                    loader?.dismiss()

                    viewModel.popup_message.value="${it?.data?.message}"
                    val successPopupFragment = SuccessPopupFragment2(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            viewModel.popup_message.value="Success"
                            viewModel?.receiveStatus?.value =it?.data?.message

                            Constants.recycleViewReceiptList.clear()

                            Constants.recycleViewReceiptList.add(ReceiptModel(type = 4))
                            Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "ACCOUNT NUMBER", reportValue =bankAccount ))
                            Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "BANK NAME", reportValue =bankName ))
                            Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "BENEFICIARY NAME", reportValue =benName ))
                            Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "RECEIPT ID", reportValue =it?.data?.receipteid.toString() ))
                            Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "Date and Time", reportValue =MethodClass.getCurrentDateTime() ))
                            //Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "SENDER NUMBER", reportValue ="9234268887" ))
                            //Constants.recycleViewReceiptList.add(ReceiptModel(type = 2, title = "TRANSACTION DATE: 2023-09-09 14:44:26" ))
                            it?.data?.transactionDetails?.let {
                                it.forEach{
                                    Constants.recycleViewReceiptList.add(ReceiptModel(type = 3, transactionId = it.transactionId.toString(), rrnId = it.utr.toString(), price = it?.transactionAmount.toString() , transactionMessage = it.remarks.toString(), userName = it.transactionStatus.toString()))
                                }
                            }


                           /* Constants.recycleViewReceiptList.add(ReceiptModel(type = 3, transactionId = "300000085", rrnId = "325220891591", price = "100" , transactionMessage = "Refund", userName = "Test User"))

                            Constants.recycleViewReceiptList.add(ReceiptModel(type = 3, transactionId = "300000085", rrnId = "325220891591", price = "100" , transactionMessage = "Refund", userName = "Test User"))

                            Constants.recycleViewReceiptList.add(ReceiptModel(type = 3, transactionId = "300000085", rrnId = "325220891591", price = "100" , transactionMessage = "Refund", userName = "Test User"))*/



                            val dialogFragment = ReceptDialogFragment(it?.data)
                            dialogFragment.show(childFragmentManager, dialogFragment.tag)
                            viewModel?.moneyTransferResponseLiveData?.value=null
                        }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)


                }
                is ResponseState.Error -> {
                    loader?.dismiss()

                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel?.moneyTransferResponseLiveData?.value=null
                }
            }
        }

       /* viewModel?.moneyTransferResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }
                is ResponseState.Success -> {
                    loader?.dismiss()
                    viewModel?.receiveStatus?.value =it?.data?.Description
                    val dialogFragment = ReceptDialogFragment()
                    dialogFragment.show(childFragmentManager, dialogFragment.tag)
                    //viewModel?.moneyTransferResponseLiveData?.value=null
                }
                is ResponseState.Error -> {
                    loader?.dismiss()

                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel?.moneyTransferResponseLiveData?.value=null
                }
            }
        }*/

    }

    private fun setRecycleView(data: ArrayList<beneficiaryListData>?) {
        binding.recycleViewBeneficiary.apply {
            beneficiaryList.clear()
            data?.forEach {
                it.apply {
                    beneficiaryList.add(BeneficiaryListModel2(recName,R.drawable.bank_imps,recBankname,"A/C:${recAcno}",recIfsc,recId))
                }

            }
           // beneficiaryList.add(BeneficiaryListModel2("Test User1",R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669",""))
           /* beneficiaryList.add(BeneficiaryListModel("Test User1",R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
            beneficiaryList.add(BeneficiaryListModel("Test User2",R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
            beneficiaryList.add(BeneficiaryListModel("Test User3",R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
            beneficiaryList.add(BeneficiaryListModel("Test User4",R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))*/
            beneficiaryListAdapter=BeneficiaryListAdapter2(beneficiaryList, object : CallBack7 {
                override fun getValue7(
                    benId: String,
                    s2: String,
                    bankNameData: String,
                    bankAccData: String,
                    beneNameData: String,
                    s6: String,
                    s7: String
                ) {
                    bankName=bankNameData
                    bankAccount=bankAccData
                    benName=beneNameData
                    activity?.let {act->
                        val selectTransactionTypeBottomSheetDialog = SelectTransactionTypemMoneyTranspherBottomSheetDialog(object :
                            CallBack {
                            override fun getValue(amount: String) {
                                val tpinBottomSheetDialog = TpinBottomSheetDialog(object :
                                    CallBack {
                                    override fun getValue(tpin: String) {
                                        // API call
                                        val (isLogin, loginResponse) = sharedPreff.getLoginData()
                                        loginResponse?.let { loginData ->
                                            loginData.userid?.let {
                                                var tType=""
                                                if (viewModel?.IMPSIsActive?.value==true){
                                                    tType="IMPS"
                                                }
                                                else if(viewModel?.NEFTIsActive?.value==true){
                                                    tType="NEFT"
                                                }
                                                val data = mapOf(
                                                    "userid" to loginData.userid,
                                                    "custid" to Constants.customerId,
                                                    "benid" to benId,
                                                    "usertpin" to tpin,
                                                    "trans_amt" to amount,
                                                    "tmode" to tType,
                                                )


                                                val gson = Gson()
                                                var jsonString = gson.toJson(data)
                                                loginData.AuthToken?.let {
                                                    viewModel?.moneyTransfer(it, jsonString.encrypt())
                                                }
                                            }
                                        }

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
                }
            })
            adapter=beneficiaryListAdapter
        }
    }

    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String): Bitmap? {
        val writer = QRCodeWriter()
        val bitMatrix: BitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, 400, 400)
        val w: Int = bitMatrix.getWidth()
        val h: Int = bitMatrix.getHeight()
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                pixels[y * w + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }
}
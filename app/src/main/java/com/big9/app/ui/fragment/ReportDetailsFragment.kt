package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R
import com.big9.app.adapter.ReportDetailsAdapter
import com.big9.app.data.model.Reportdetails
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentReportDetailsBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants.reportDetailsAdapter
import com.big9.app.utils.helpers.Constants.reportDetailsPropertyList
import com.google.gson.Gson

class ReportDetailsFragment : BaseFragment() {
    private var loader: Dialog? = null
    lateinit var binding: FragmentReportDetailsBinding
    private val viewModel: MyViewModel by activityViewModels()

    var jsonData:String=""
    private var lastClickTime1: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_details, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("jsonData")?.let {
            jsonData=it
        }
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {

        binding.apply {
            fabShare.setOnClickListener{
                shareImage()
            }
           /* imgBtnShare.setOnClickListener{

            }*/
          imgBack.back()

          }
        }


    fun initView() {
        reportDetailsPropertyList?.clear()
        reportDetailsAdapter?.items=ArrayList()
        reportDetailsAdapter?.notifyDataSetChanged()
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
        binding.recycleViewReportdetails.apply {
            reportDetailsAdapter= ReportDetailsAdapter(ArrayList())

            //reportDetailsPropertyList.add(Reportdetails("Transaction id","001"))
            adapter=reportDetailsAdapter
        }
            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            if (isLogin){
                loginResponse?.let {loginData->
                    viewModel?.apply {
                        reportDetailsPropertyList?.clear()
                        reportDetailsAdapter?.items=ArrayList()
                        reportDetailsAdapter?.notifyDataSetChanged()
                        val  data = mapOf(
                            "userid" to loginData.userid,
                            "idp" to viewModel.reportTypeIDRecept.value
                        )

                        val gson= Gson()
                        var jsonString = gson.toJson(data)
                        if (viewModel.reportType.value==getString(R.string.transactions)) {
                            loginData.AuthToken?.let {
                                /*if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                    return
                                }*/
                                transcationReportReceipt(it, jsonString.encrypt())
                            }
                        }

                        if (viewModel.reportType.value==getString(R.string.dmt)) {
                            loginData.AuthToken?.let {
                                /*if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                    return
                                }*/
                                dmtReportReceipt(it, jsonString.encrypt())
                            }
                        }

                        if (viewModel.reportType.value==getString(R.string.aeps)) {
                            loginData.AuthToken?.let {
                                /*if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                    return
                                }*/
                                aepsReportReceiptReceipt(it, jsonString.encrypt())
                            }
                        }
                        if (viewModel.reportType.value==getString(R.string.micro_atm)) {
                            loginData.AuthToken?.let {
                                /*if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                    return
                                }*/
                                microatmReportReceipt(it, jsonString.encrypt())
                            }
                        }





                    }

                }
            }

        /*try {
            val jsonObject = JSONObject(jsonData)
            val propertyNames = jsonObject.keys()
            reportDetailsPropertyList.clear()
            for (propertyName in propertyNames) {
                reportDetailsPropertyList.add(Reportdetails(propertyName,jsonObject.getString("$propertyName")))
            }
            *//*for (propertyName in propertyNames) {
                println("Property Name: $propertyName")
                val value1 = jsonObject.getString("$propertyName")
                Log.d("TAG_value", "initView: "+value1)


                val textView = TextView(binding.root.context)
                textView.text = "$propertyName : $value1"
                textView.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                // Customize the appearance (e.g., text size, text color, etc.)
                textView.textSize = 18f
                textView.setTextColor(resources.getColor(android.R.color.black))

                // Add the dynamic TextView to the parent layout
                binding.linearLayout.addView(textView)


            }*//*
            //Share data
            binding.imgBtnShare.setOnClickListener{
                shareImage()
                *//*val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, jsonObject.toString())
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Share using"))*//*
            }
        } catch (e: Exception) {
            // Handle any potential JSON parsing exceptions here
        }*/


    }
    private fun shareImage() {
        activity?.let {
            binding.apply {
                var screenshotBitmap =cardView2.takeScreenshot2()
                (activity as? DashboardActivity)?.shareImage(screenshotBitmap)
            }
        }
    }
    fun setObserver() {
        viewModel?.transcationReportReceiptResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    if (it.data?.responseCode==200) {
                        //reportDetailsPropertyList.clear()
                        it.data.data?.let { dataList ->
                            for (transactionData in dataList) {
                               // val fields: Array<Field> = transactionData.javaClass.declaredFields

                             //   for (field in fields) {
                                    /*field.isAccessible = true
                                    val key = field.name
                                    var value =""
                                    field.get(transactionData)?.let {
                                         value = field.get(transactionData) as String
                                    }*/
                                    //@/*SerializedName("transaction_id") var transactionId: String? = null,
                                   /* @SerializedName("customer_number") var customerNumber: String? = null,
                                    @SerializedName("status") var status: String? = null,
                                    @SerializedName("amount") var amount: String? = null,
                                    @SerializedName("balance") var balance: String? = null,
                                    @SerializedName("operator") var operator: String? = null,
                                    @SerializedName("operatorID") var operatorID: String? = null*/
                                    //reportDetailsPropertyList.add(Reportdetails(property=key.addSpaceBeforeUppercase(),reportValue=value))
                                if(!transactionData.transactionId.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Transaction Id",reportValue=transactionData.transactionId))
                                }
                                if(!transactionData.customerNumber.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Customer Number",reportValue=transactionData.customerNumber))
                                }
                                if(!transactionData.status.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Status",reportValue=transactionData.status))
                                }
                                if(!transactionData.amount.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Amount",reportValue=transactionData.amount))
                                }
                                if(!transactionData.balance.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Balance",reportValue=transactionData.balance))
                                }
                                if(!transactionData.operator.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Operator",reportValue=transactionData.operator))
                                }
                                /*if(!transactionData.operator.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Operator",reportValue=transactionData.operator))
                                }*/





                               // }
                            }
                        }


                     /*   it.data.data?.let {dataList->

                            for (responseData in dataList) {
                                for ((key, value) in responseData) {

                                }
                                reportDetailsPropertyList.add(Reportdetails(property=key,reportValue=value))
                                println("Key: $key, Value: $value")
                            }

                    }*/


                            /*it.data.data?.let {
                                Toast.makeText(requireContext(), ""+it, Toast.LENGTH_SHORT).show()
                                for((key,value) in it){
                                    reportDetailsPropertyList.add(Reportdetails(property=key,reportValue=value))

                                }
                            }*/


                        reportDetailsAdapter?.items=reportDetailsPropertyList
                        reportDetailsAdapter?.notifyDataSetChanged()
                        viewModel?.transcationReportReceiptResponseLiveData?.value=null
                    }
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel?.transcationReportReceiptResponseLiveData?.value=null
                }
            }
        }

        viewModel?.dmtReportReceiptResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    if (it.data?.responseCode==200) {
                        reportDetailsPropertyList.clear()
                        it.data.data?.let { dataList ->
                            for (transactionData in dataList) {




                                if(!transactionData.tranId.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Transaction Id",reportValue=transactionData.tranId))
                                }
                                if(!transactionData.tranAmt.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Transaction Amount",reportValue=transactionData.tranAmt))
                                }
                                if(!transactionData.recName.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Receiver Name",reportValue=transactionData.recName))
                                }
                                if(!transactionData.tranStatus.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Status",reportValue=transactionData.tranStatus))
                                }
                                if(!transactionData.transDt.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Transaction Date",reportValue=transactionData.transDt))
                                }
                                if(!transactionData.utr.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="UTR",reportValue=transactionData.utr))
                                }
                                if(!transactionData.recAcno.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Receiver Account Number",reportValue=transactionData.recAcno))
                                }
                                if(!transactionData.totalTranAmt.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Total Transaction Amount",reportValue=transactionData.totalTranAmt))
                                }
                                if(!transactionData.bankName.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Bank Name",reportValue=transactionData.bankName))
                                }
                                if(!transactionData.custMobno.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Customer Mobile Number",reportValue=transactionData.custMobno))
                                }



                                //val fields: Array<Field> = transactionData.javaClass.declaredFields

                               /* for (field in fields) {
                                    field.isAccessible = true
                                    val key = field.name
                                    var value =""
                                    field.get(transactionData)?.let {
                                        value = field.get(transactionData) as String
                                    }
                                    reportDetailsPropertyList.add(Reportdetails(property=key.addSpaceBeforeUppercase(),reportValue=value))

                                    */   /* var  key2=if (key=="tran_amt"){
                                            "Transaction Id"
                                        }
                                        else if (key=="tran_status"){
                                            "Customer Number"
                                        }
                                        else if (key=="trans_dt"){
                                            "Status"
                                        }
                                        else if (key=="rec_name"){
                                            "Amount"
                                        }
                                        else if (key=="rec_acno"){
                                            "Balance"
                                        }
                                        else if (key=="total_tran_amt"){
                                            "Operator"
                                        }
                                        else if (key=="bank_name"){
                                            "Operator ID"
                                        }
                                        else if (key=="cust_mobno"){
                                            "Operator ID"
                                        }


                                        else{
                                            ""
                                        }



                                    if (key2!=""){
                                        reportDetailsPropertyList.add(Reportdetails(property=key,reportValue=value))
                                    }*/


                               // }
                            }
                        }




                        reportDetailsAdapter?.items=reportDetailsPropertyList
                        reportDetailsAdapter?.notifyDataSetChanged()
                        viewModel?.transcationReportReceiptResponseLiveData?.value=null
                    }
                    viewModel?.dmtReportReceiptResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    viewModel?.dmtReportReceiptResponseLiveData?.value=null
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }

        viewModel?.aepsReportReceiptReceiptResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                        reportDetailsPropertyList.clear()
                        it.data?.data?.let { dataList ->
                            for (transactionData in dataList) {

                                if(!transactionData.transactionId.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Transaction Id",reportValue=transactionData.transactionId))
                                }
                                if(!transactionData.customerNumber.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Customer Number",reportValue=transactionData.customerNumber))
                                }
                                if(!transactionData.status.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Status",reportValue=transactionData.status))
                                }
                                if(!transactionData.amount.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Amount",reportValue=transactionData.amount))
                                }
                                if(!transactionData.balance.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Balance",reportValue=transactionData.balance))
                                }
                                if(!transactionData.operator.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Operator",reportValue=transactionData.operator))
                                }
                                if(!transactionData.operatorID.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Operator ID",reportValue=transactionData.operatorID))
                                }
                                if(!transactionData.currentBalance.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Current Balance",reportValue=transactionData.currentBalance))
                                }
                                if(!transactionData.TransactionDate.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Transaction Date",reportValue=transactionData.TransactionDate))
                                }


                                //val fields: Array<Field> = transactionData.javaClass.declaredFields

                                /*for (field in fields) {
                                    field.isAccessible = true
                                    var key = field.name

                                    var value ="-"
                                    field.get(transactionData)?.let {
                                        value = field.get(transactionData) as String
                                    }
                                    reportDetailsPropertyList.add(Reportdetails(property=key.addSpaceBeforeUppercase(),reportValue=value.replace("_"," ")))
                                  */
                                    /*  Log.d("TAG_key", "setObserver: "+key+" value:"+value)
                                   if (key=="transaction_id"){


                                    }
                                    else if (key=="customer_number"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Customer Number",reportValue=value))

                                    }
                                   else if (key=="amount"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Amount",reportValue=value))

                                   }
                                    else if (key== "TransactionDate"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Transaction Date",reportValue=value))
                                    }
                                     else if (key== "operator"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Operator",reportValue=value.replace("-"," ")))
                                    }


                                    else if (key=="current_balance"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Current Balance",reportValue=value))

                                    }

*/

                                //}
                            }
                        }

                        reportDetailsAdapter?.items=reportDetailsPropertyList
                        reportDetailsAdapter?.notifyDataSetChanged()
                        viewModel?.transcationReportReceiptResponseLiveData?.value=null

                    viewModel?.aepsReportReceiptReceiptResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    viewModel?.aepsReportReceiptReceiptResponseLiveData?.value=null
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


        viewModel?.microatm_report_receiptResponseReceptLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                        //reportDetailsPropertyList.clear()
                        it.data?.data?.let { dataList ->
                            for (transactionData in dataList) {
/*
    @SerializedName("transaction_id") var transactionId: String? = null,
    @SerializedName("customer_number") var customerNumber: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("operator") var operator: String? = null,
    @SerializedName("operatorID") var operatorID: String? = null,
    @SerializedName("BankReferenceNumber") var BankReferenceNumber: String? = null,
    @SerializedName("masked_pan") var maskedPan: String? = null,
    @SerializedName("avbalance") var avbalance: String? = null,
    @SerializedName("trans_dt") var transDt: String? = null,
    @SerializedName("response_description") var responseDescription: String? = null
 */

                                if(!transactionData.transactionId.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Transaction Id",reportValue=transactionData.transactionId))
                                }
                                if(!transactionData.customerNumber.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Customer Number",reportValue=transactionData.customerNumber))
                                }

                                if(!transactionData.amount.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Amount",reportValue=transactionData.amount))
                                }

                                if(!transactionData.operator.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Operator",reportValue=transactionData.operator))
                                }
                                if(!transactionData.BankReferenceNumber.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Bank Reference Number",reportValue=transactionData.BankReferenceNumber))
                                }
                                if(!transactionData.maskedPan.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Pancard Number",reportValue=transactionData.maskedPan))
                                }
                                if(!transactionData.avbalance.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Available Balance",reportValue=transactionData.avbalance))
                                }
                                if(!transactionData.responseDescription.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="",reportValue=transactionData.responseDescription))
                                }
                                if(!transactionData.transDt.isNullOrEmpty()){
                                    reportDetailsPropertyList.add(Reportdetails(property="Transaction Date",reportValue=transactionData.transDt))
                                }


                                /*val fields: Array<Field> = transactionData.javaClass.declaredFields

                                for (field in fields) {
                                    field.isAccessible = true
                                    var key = field.name

                                    var value =""
                                    field.get(transactionData)?.let {
                                        value = field.get(transactionData) as String
                                    }

                                    reportDetailsPropertyList.add(
                                        Reportdetails(
                                            property = key.addSpaceBeforeUppercase(),
                                            reportValue = value
                                        )
                                    )


                                    var key2=if (key=="customer_number"){
                                        "Customer Number"
                                    }


                                    else if (key=="amount"){
                                        "Amount"
                                    }
                                    else if (key=="operator"){
                                        ""
                                    }
                                    else if (key=="BankReferenceNumber"){
                                        "Bank Reference Number"
                                    }
                                    else if (key=="avbalance"){
                                        "Available Balance"
                                    }
                                    else if (key=="response_description"){
                                        "Description "
                                    }
                                    else if (key=="operator"){
                                        ""
                                    }

                                    else{
                                        ""
                                    }
                                    if (key2!="") {
                                        reportDetailsPropertyList.add(
                                            Reportdetails(
                                                property = key2,
                                                reportValue = value
                                            )
                                        )
                                    }

                                }*/
                            }
                        }





                        reportDetailsAdapter?.items=reportDetailsPropertyList
                        reportDetailsAdapter?.notifyDataSetChanged()
                        viewModel?.transcationReportReceiptResponseLiveData?.value=null

                    viewModel?.microatm_report_receiptResponseReceptLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    viewModel?.microatm_report_receiptResponseReceptLiveData?.value=null
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }






    }

    fun String.addSpaceBeforeUppercase(): String {
        var temp:String=this.lowercase()
            .replace("trans","Transaction ")
            .replace("tran","Transaction ")

            .replace("dt","Date ")
            .replace("amt","Amount ")
            .replace("rec","Receiver ")
            .replace("acno","Account No.: ")
            .replace("cust","Customer ")
            .replace("mobno","Mobile No.: ")
        return temp.replace(Regex("(?<=\\p{Ll})(?=\\p{Lu})"), " ").capitalize()
    }


}
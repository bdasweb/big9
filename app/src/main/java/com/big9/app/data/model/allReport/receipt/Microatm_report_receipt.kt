package com.big9.app.data.model.allReport.receipt

import com.google.gson.annotations.SerializedName

class Microatm_report_receipt {

    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<Microatm_reportData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class Microatm_reportData(

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

)




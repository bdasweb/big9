package com.big9.app.data.model.allReport.receipt

import com.google.gson.annotations.SerializedName

class Aeps_report_receiptModel {

    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<Aeps_report_receiptData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class Aeps_report_receiptData(

    @SerializedName("transaction_id") var transactionId: String? = null,
    @SerializedName("customer_number") var customerNumber: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("TransactionDate") var TransactionDate: String? = null,
    @SerializedName("operator") var operator: String? = null,
    @SerializedName("operatorID") var operatorID: String? = null,
    @SerializedName("current_balance") var currentBalance: String? = null

)




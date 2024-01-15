package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class AEPSReportModel(
    @SerializedName("Description"   ) var Description  : String?         = null,
    @SerializedName("response_code" ) var responseCode : Int?            = null,
    @SerializedName("data"          ) var data         : ArrayList<AEPSReportData> = arrayListOf(),
    @SerializedName("timestamp"     ) var timestamp    : String?         = null

)

data class AEPSReportData(
    @SerializedName("transaction_id") var transactionId: String? = null,
    @SerializedName("customer_number") var customerNumber: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("balance") var balance: String? = null,
    @SerializedName("operator") var operator: String? = null,
    @SerializedName("operatorID") var operatorID: String? = null,
    @SerializedName("current_balance") var currentBalance: String? = null,
    @SerializedName("TransactionDate") var TransactionDate: String? = null
)

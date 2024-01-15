package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class TransactionReportResponse {

    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<TransactionData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class TransactionData(

    @SerializedName("ID") var ID: String? = null,
    @SerializedName("logo") var logo: String? = null,
    @SerializedName("Amount") var Amount: String? = null,
    @SerializedName("Transaction_ID") var TransactionID: String? = null,
    @SerializedName("Date") var tDate: String? = null,
    @SerializedName("Status") var Status: String? = null,
    @SerializedName("Operator") var Operator: String? = null,
    @SerializedName("CustNo") var CustNo: String? = null,
    @SerializedName("referenceID") var referenceID: String? = null

)




package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class CreditCardVerifyOtpModel(
    @SerializedName("Description")
    var Description: String? = null,
    @SerializedName("response_code")
    var responseCode: Int? = null,
    @SerializedName("data")
    var data: ArrayList<CreditCardVerifyOtpData> = arrayListOf(),
    @SerializedName("state")
    var state: ArrayList<String> = arrayListOf(),
    @SerializedName("timestamp")
    var timestamp: String? = null,

    )

data class CreditCardVerifyOtpData(
    @SerializedName("userid") var userid: String? = null,
    @SerializedName("ref_id") var refId: String? = null,
    @SerializedName("transDate") var transDate: String? = null,
    @SerializedName("utr") var utr: String? = null,
    @SerializedName("intid") var intid: String? = null,
    @SerializedName("network") var network: String? = null,
    @SerializedName("usercurrbal") var usercurrbal: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("status") var status: String? = null

)

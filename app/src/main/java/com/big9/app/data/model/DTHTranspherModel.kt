package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class DTHTranspherModel(
    @SerializedName("Description")
    var Description: String? = null,
    @SerializedName("response_code")
    var responseCode: Int? = null,

    @SerializedName("data")
    var data: ArrayList<DTHTranspherData> = arrayListOf(),

    @SerializedName("state")
    var state: ArrayList<String> = arrayListOf(),

    @SerializedName("timestamp")
    var timestamp: String? = null,

    )


data class DTHTranspherData(
    @SerializedName("userid") var userid: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("refillid") var refillid: String? = null,
    @SerializedName("subscriber_id") var subscriberId: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("curramt") var curramt: String? = null,
    @SerializedName("operator") var operator: String? = null,
    @SerializedName("operatorid") var operatorid: String? = null

)



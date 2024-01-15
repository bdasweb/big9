package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class DTHOperatorModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: ArrayList<DTHOperatorData> = arrayListOf(),
    @SerializedName("state") var state: String? = null,
    @SerializedName("timestamp") var timestamp: String? = null

)


data class DTHOperatorData(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("mobileNo") var mobileNo: String? = null,
    @SerializedName("LastTransactionAmount") var LastTransactionAmount: String? = null,
    @SerializedName("curramt") var curramt: String? = null,


)

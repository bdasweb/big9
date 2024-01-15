package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class EPotlyTranspherModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var epotlyData: EpotlyData? = EpotlyData()

)


data class EpotlyData(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("mobileNo") var mobileNo: String? = null,
    @SerializedName("LastTransactionAmount") var LastTransactionAmount: String? = null,
    @SerializedName("curramt") var curramt: String? = null

)

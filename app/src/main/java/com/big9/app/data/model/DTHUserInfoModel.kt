package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class DTHUserInfoModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: ArrayList<DTHUserInfoData> = arrayListOf(),
    @SerializedName("timestamp") var timestamp: String? = null
)


data class DTHUserInfoData(

    @SerializedName("customerName") var customerName: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("Balance") var Balance: String? = null,
    @SerializedName("NextRechargeDate") var NextRechargeDate: String? = null,
    @SerializedName("MonthlyRecharge") var MonthlyRecharge: String? = null

)

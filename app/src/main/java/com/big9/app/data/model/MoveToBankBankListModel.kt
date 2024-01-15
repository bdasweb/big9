package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class MoveToBankBankListModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: ArrayList<MoveToBankBankListData> = arrayListOf(),
    @SerializedName("timestamp") var timestamp: String? = null
)

data class MoveToBankBankListData(
    @SerializedName("acno") var acno: String? = null,
    @SerializedName("ifsc") var ifsc: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("delflag") var delflag: String? = null,
    @SerializedName("delflag_status") var delflagStatus: String? = null
)




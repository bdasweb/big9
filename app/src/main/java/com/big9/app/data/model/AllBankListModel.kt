package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class AllBankListModel(

    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: ArrayList<AllBankListData> = arrayListOf(),
    @SerializedName("timestamp") var timestamp: String? = null
)

data class AllBankListData(

    @SerializedName("bank_id") var bankId: String? = null,
    @SerializedName("logo") var logo: String? = null,
    @SerializedName("bank_name") var bankName: String? = null,
    @SerializedName("accno") var accno: String? = null,
    @SerializedName("ifscCode") var ifscCode: String? = null
)


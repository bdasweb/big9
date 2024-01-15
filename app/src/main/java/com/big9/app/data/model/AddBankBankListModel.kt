package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class AddBankBankListModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: ArrayList<AddBankBankListData> = arrayListOf(),
    @SerializedName("timestamp") var timestamp: String? = null

)


data class AddBankBankListData(

    @SerializedName("name") var name: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("ifsc") var ifsc: String? = null

)

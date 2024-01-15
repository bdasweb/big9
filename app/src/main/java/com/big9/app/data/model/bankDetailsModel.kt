package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class bankDetailsModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: ArrayList<banknameData> = arrayListOf(),
    @SerializedName("timestamp") var timestamp: String? = null
)



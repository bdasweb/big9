package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class CreditCardSendOtpModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("ID") var ID: Int? = null,
    @SerializedName("stateresp") var stateresp: String? = null

)

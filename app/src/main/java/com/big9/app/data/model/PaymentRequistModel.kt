package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class PaymentRequistModel(

    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: String? = null,
    @SerializedName("ID") var ID: String? = null,
    @SerializedName("timestamp") var timestamp: String? = null
)



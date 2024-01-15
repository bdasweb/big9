package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class AddBankModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null

)



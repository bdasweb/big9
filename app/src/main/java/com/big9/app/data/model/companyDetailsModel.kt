package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class companyDetailsModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var response_code: String? = null
)

package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class ServiceCheckModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("slug") var slug: String? = null
)

package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class CheckServiceModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var response_code: Int? = null,
    @SerializedName("slug") var slug: String? = null


)


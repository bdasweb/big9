package com.big9.app.data.genericmodel

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: T? = null

)




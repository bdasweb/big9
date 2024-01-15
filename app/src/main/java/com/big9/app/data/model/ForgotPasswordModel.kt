package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordModel(
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("timestamp") var timestamp: String? = null,
    @SerializedName("userID") var userID: String? = null,
    @SerializedName("Auth_token") var AuthToken: String? = null,
    @SerializedName("refresh_token") var refreshToken: String? = null

)





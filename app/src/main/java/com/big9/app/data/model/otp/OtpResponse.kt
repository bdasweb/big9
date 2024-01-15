package com.big9.app.data.model.otp

import com.google.gson.annotations.SerializedName

class OtpResponse {
    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("userid")
    var userid: String? = null
    @SerializedName("step")
    var step: Int? = null
    @SerializedName("timestamp")
    var timestamp: String? = null


}


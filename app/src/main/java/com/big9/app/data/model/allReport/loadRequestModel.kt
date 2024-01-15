package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class loadRequestModel {

    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<loadRequestData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class loadRequestData(

    @SerializedName("purchaseid") var purchaseid: String? = null,
    @SerializedName("Amount") var Amount: String? = null,
    @SerializedName("insdate") var insdate: String? = null,
    @SerializedName("isdone") var isdone: String? = null,
    @SerializedName("bankname") var bankname: String? = null,
    @SerializedName("isdonedate") var isdonedate: String? = null

)




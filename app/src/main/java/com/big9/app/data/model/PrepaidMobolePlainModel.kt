package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

class PrepaidMobolePlainModel {


    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<MobilePrepaidPlainData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null

}

data class MobilePrepaidPlainData(

    @SerializedName("rs") var rs: String? = null,
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("validity") var validity: String? = null

)




package com.big9.app.data.model.allReport

import com.big9.app.data.model.MobilePrepaidData
import com.google.gson.annotations.SerializedName

class PostPaidMobileTranspherModel {
    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<MobilePostpaidData2> = arrayListOf()
    @SerializedName("state")
    var state: ArrayList<String> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}


data class MobilePostpaidData2(

    @SerializedName("userid") var userid: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("refillid") var refillid: String? = null,
    @SerializedName("mobileno") var mobileno: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("curramt") var curramt: String? = null,
    @SerializedName("operator") var operator: String? = null,
    @SerializedName("operatorid") var operatorid: String? = null,
    var image:String?=""

)




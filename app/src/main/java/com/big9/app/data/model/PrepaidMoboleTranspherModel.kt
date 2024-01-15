package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

class PrepaidMoboleTranspherModel {
    @SerializedName("Description"   ) var Description  : String?         = null
    @SerializedName("response_code" ) var responseCode : Int?            = null
    @SerializedName("data"          ) var data         : ArrayList<PrepaidMoboleTranspherData> = arrayListOf()
    @SerializedName("timestamp"     ) var timestamp    : String?         = null





    /* @SerializedName("userid"     ) var userid     : String? = null
     @SerializedName("status"     ) var status     : String? = null
     @SerializedName("refillid"   ) var refillid   : String? = null
     @SerializedName("mobileno"   ) var mobileno   : String? = null
     @SerializedName("amount"     ) var amount     : String? = null
     @SerializedName("curramt"    ) var curramt    : String? = null
     @SerializedName("operator"   ) var operator   : String? = null
     @SerializedName("operatorid" ) var operatorid : String? = null
     var image:String?=""*/

}

class PrepaidMoboleTranspherData {
    @SerializedName("userid")
    var userid: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("refillid")
    var refillid: String? = null
    @SerializedName("mobileno")
    var mobileno: String? = null
    @SerializedName("amount")
    var amount: String? = null
    @SerializedName("curramt")
    var curramt: String? = null
    @SerializedName("operator")
    var operator: String? = null
    @SerializedName("operatorid")
    var operatorid: String? = null




}



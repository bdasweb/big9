import com.google.gson.annotations.SerializedName

class checkUserModel {
    @SerializedName("message"  ) var message  : String?   = null
    @SerializedName("status"   ) var status   : String?      = null
    @SerializedName("stateresp"   ) var stateresp   : String?      = null
    @SerializedName("mobile"   ) var mobile   : String?   = null
    @SerializedName("custID"   ) var custID   : String?   = null
    @SerializedName("custType" ) var custType : String?   = null
    @SerializedName("response" ) var response : Response? = Response()
}


data class Response (

    @SerializedName("fname"        ) var fname       : String? = null,
    @SerializedName("lname"        ) var lname       : String? = null,
    @SerializedName("mobile"       ) var mobile      : String? = null,
    @SerializedName("status"       ) var status      : String? = null,
    @SerializedName("bank3_limit"  ) var bank3Limit  : Int?    = null,
    @SerializedName("bank3_status" ) var bank3Status : String? = null,
    @SerializedName("bank2_limit"  ) var bank2Limit  : Int?    = null,
    @SerializedName("bank1_limit"  ) var bank1Limit  : Int?    = null

)
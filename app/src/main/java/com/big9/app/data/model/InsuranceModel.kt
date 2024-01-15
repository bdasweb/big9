import com.google.gson.annotations.SerializedName

class InsuranceModel {
    @SerializedName("message" ) var message : String? = null
    @SerializedName("refid"   ) var refid   : Int?    = null
    @SerializedName("data"    ) var data    : InsuranceData?   = InsuranceData()
    @SerializedName("status"  ) var status  : Int?    = null
}

class InsuranceData {
    @SerializedName("redirecturl" ) var redirecturl : String? = null
    @SerializedName("ret_data"    ) var retData     : String? = null
}


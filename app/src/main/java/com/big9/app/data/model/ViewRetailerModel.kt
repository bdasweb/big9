import com.google.gson.annotations.SerializedName

class ViewRetailerModel {
    @SerializedName("Description"   ) var Description  : String?         = null
    @SerializedName("data"          ) var data         : ArrayList<ViewRetailerData> = arrayListOf()
    @SerializedName("response_code" ) var responseCode : Int?            = null
    @SerializedName("timestamp"     ) var timestamp    : String?         = null
}

class ViewRetailerData {
    @SerializedName("ID"           ) var ID          : String? = null
    @SerializedName("name"         ) var name        : String? = null
    @SerializedName("mobileNo"     ) var mobileNo    : String? = null
    @SerializedName("curr_balance" ) var currBalance : String? = null
    @SerializedName("bname"        ) var bname       : String? = null
}



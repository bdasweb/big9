package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

class PrePaidMobileOperatorListModel {

    @SerializedName("Description"   ) var Description  : String?           = null
    @SerializedName("response_code" ) var responseCode : Int?              = null
    @SerializedName("data"          ) var data         : ArrayList<MobilePrepaidData>   = arrayListOf()
    @SerializedName("state"         ) var state        : ArrayList<String> = arrayListOf()
    @SerializedName("timestamp"     ) var timestamp    : String?           = null

}

data class MobilePrepaidData(

    @SerializedName("opname"      ) var opname      : String? = null,
    @SerializedName("opcode"      ) var opcode      : String? = null,
    @SerializedName("imglink"     ) var imglink     : String? = null,
    @SerializedName("minrecharge" ) var minrecharge : String? = null,
    @SerializedName("maxrecharge" ) var maxrecharge : String? = null,
    @SerializedName("minlen"      ) var minlen      : String? = null,
    @SerializedName("maxlen"      ) var maxlen      : String? = null

)




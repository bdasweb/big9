package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class PostPaidMobileOperatorListModel {

    @SerializedName("Description"   ) var Description  : String?         = null
    @SerializedName("response_code" ) var responseCode : Int?            = null
    @SerializedName("data"          ) var data         : ArrayList<MobilePostpaidData> = arrayListOf()
    @SerializedName("state"         ) var state        : String?         = null
    @SerializedName("timestamp"     ) var timestamp    : String?         = null


}

data class MobilePostpaidData(

    @SerializedName("opname"      ) var opname      : String? = null,
    @SerializedName("opcode"      ) var opcode      : String? = null,
    @SerializedName("imglink"     ) var imglink     : String? = null,
    @SerializedName("minrecharge" ) var minrecharge : String? = null,
    @SerializedName("maxrecharge" ) var maxrecharge : String? = null,
    @SerializedName("minlen"      ) var minlen      : String? = null,
    @SerializedName("maxlen"      ) var maxlen      : String? = null

)




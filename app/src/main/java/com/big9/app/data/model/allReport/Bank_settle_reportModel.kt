package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class Bank_settle_reportModel {

    @SerializedName("Description")
    var Description: String? = null

    @SerializedName("response_code")
    var responseCode: Int? = null

    @SerializedName("data")
    var data: ArrayList<Bank_settle_reportData> = arrayListOf()

    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class Bank_settle_reportData(

    @SerializedName("tran_id") var tranId: String? = null,
    @SerializedName("tran_amt") var tranAmt: String? = null,
    @SerializedName("trans_dt") var transDt: String? = null,
    @SerializedName("utr") var utr: String? = null,
    @SerializedName("ekotrans_id") var ekotransId: String? = null

)




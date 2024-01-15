package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class WalletSettleReportModel {

    @SerializedName("Description")
    var Description: String? = null

    @SerializedName("response_code")
    var responseCode: Int? = null

    @SerializedName("data")
    var data: ArrayList<WalletSettleReportData> = arrayListOf()

    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class WalletSettleReportData(

    @SerializedName("tran_id") var tranId: String? = null,
    @SerializedName("tran_amt") var tranAmt: String? = null,
    @SerializedName("trans_dt") var transDt: String? = null,
    @SerializedName("utr") var utr: String? = null,
    @SerializedName("ekotrans_id") var ekotransId: String? = null

)




package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class DmtReportReportModel {

    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<DmtData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class DmtData(

    @SerializedName("receiptid") var receiptid: String? = null,
    @SerializedName("tran_id") var tranId: String? = null,
    @SerializedName("tran_amt") var tranAmt: String? = null,
    @SerializedName("trans_dt") var transDt: String? = null,
    @SerializedName("rec_name") var recName: String? = null,
    @SerializedName("rec_acno") var recAcno: String? = null,
    @SerializedName("cust_mobno") var custMobno: String? = null,
    @SerializedName("tran_status") var tranStatus: String? = null,
    @SerializedName("utr") var utr: String? = null,
    @SerializedName("type") var type: String? = null

)




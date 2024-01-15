package com.big9.app.data.model.allReport.receipt

import com.google.gson.annotations.SerializedName

class Dmt_report_receiptModel {

    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<Dmt_report_receiptModelData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class Dmt_report_receiptModelData(

    @SerializedName("tran_id") var tranId: String? = null,
    @SerializedName("tran_amt") var tranAmt: String? = null,
    @SerializedName("rec_name") var recName: String? = null,
    @SerializedName("tran_status") var tranStatus: String? = null,
    @SerializedName("trans_dt") var transDt: String? = null,
    @SerializedName("utr") var utr: String? = null,
    @SerializedName("rec_acno") var recAcno: String? = null,
    @SerializedName("total_tran_amt") var totalTranAmt: String? = null,
    @SerializedName("bank_name") var bankName: String? = null,
    @SerializedName("cust_mobno") var custMobno: String? = null
)




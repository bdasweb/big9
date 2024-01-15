package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class MicroatmReportModel {

    @SerializedName("Description")
    var Description: String? = null

    @SerializedName("response_code")
    var responseCode: Int? = null

    @SerializedName("data")
    var data: ArrayList<MicroatmReportData> = arrayListOf()

    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class MicroatmReportData(

    @SerializedName("tran_id") var tranId: String? = null,
    @SerializedName("tran_status") var tranStatus: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("tran_amt") var tranAmt: String? = null,
    @SerializedName("response_description") var responseDescription: String? = null,
    @SerializedName("masked_pan") var maskedPan: String? = null,
    @SerializedName("BankReferenceNumber") var BankReferenceNumber: String? = null,
    @SerializedName("trans_dt") var transDt: String? = null

)




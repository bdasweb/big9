package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class aepsReportModel {

    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<aepsReportData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class aepsReportData(

    @SerializedName("tran_id") var tranId: String? = null,
    @SerializedName("tran_status") var tranStatus: String? = null,
    @SerializedName("tran_amt") var tranAmt: String? = null,
    @SerializedName("response_description") var responseDescription: String? = null,
    @SerializedName("aadharno") var aadharno: String? = null,
    @SerializedName("BankReferenceNumber") var BankReferenceNumber: String? = null,
    @SerializedName("avbalance") var avbalance: String? = null,
    @SerializedName("trans_dt") var transDt: String? = null,
    @SerializedName("type") var type: String? = null

)




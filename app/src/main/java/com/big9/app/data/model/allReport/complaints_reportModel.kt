package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class complaints_reportMode {


    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<complaints_reportData2> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class complaints_reportData2(

    @SerializedName("ddlComplaintCategory") var ddlComplaintCategory: String? = null,
    @SerializedName("ddlComplaintType") var ddlComplaintType: String? = null,
    @SerializedName("ticketID") var ticketID: String? = null,
    @SerializedName("ticketDate") var ticketDate: String? = null,
    @SerializedName("txtTransactionId") var txtTransactionId: String? = null,
    @SerializedName("ticketFlag") var ticketFlag: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("complaintCategoryName") var complaintCategoryName: String? = null,
    @SerializedName("complaintTypeName") var complaintTypeName: String? = null

)




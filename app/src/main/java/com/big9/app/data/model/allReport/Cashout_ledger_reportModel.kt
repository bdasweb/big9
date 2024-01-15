package com.big9.app.data.model.allReport

import com.google.gson.annotations.SerializedName

class Cashout_ledger_reportModel {

    @SerializedName("Description")
    var Description: String? = null

    @SerializedName("response_code")
    var responseCode: Int? = null

    @SerializedName("data")
    var data: ArrayList<Cashout_ledger_reportData> = arrayListOf()

    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class Cashout_ledger_reportData(

    @SerializedName("refillid") var refillid: String? = null,
    @SerializedName("iid") var iid: String? = null,
    @SerializedName("insdate") var insdate: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("curramt") var curramt: String? = null

)




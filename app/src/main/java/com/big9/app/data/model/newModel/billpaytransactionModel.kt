import com.google.gson.annotations.SerializedName

class billpaytransactionModel {
    @SerializedName("message")
    var message: String? = null
    @SerializedName("status")
    var status: Int? = null
    @SerializedName("ref_id")
    var refId: Int? = null
    @SerializedName("data")
    var data: billpaytransactionData? = billpaytransactionData()
}

data class billpaytransactionData(

    @SerializedName("customer_id") var customerId: String? = null,
    @SerializedName("txnAmount") var txnAmount: String? = null,
    @SerializedName("txnDate") var txnDate: String? = null,
    @SerializedName("txnID") var txnID: Int? = null,
    @SerializedName("txnstatue") var txnstatue: String? = null,
    @SerializedName("integratorid") var integratorid: String? = null,
    @SerializedName("operatorid") var operatorid: String? = null

)
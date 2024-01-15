import com.google.gson.annotations.SerializedName

class MoneyTranspherModel {

    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status: Int? = null

    @SerializedName("total_amount")
    var totalAmount: String? = null

    @SerializedName("userID")
    var userID: String? = null

    @SerializedName("receipteid")
    var receipteid: Int? = null

    @SerializedName("transaction_details")
    var transactionDetails: ArrayList<TransactionDetails> = arrayListOf()
}


data class TransactionDetails(

    @SerializedName("transaction_amount") var transactionAmount: String? = null,
    @SerializedName("transaction_id") var transactionId: Int? = null,
    @SerializedName("utr") var utr: String? = null,
    @SerializedName("remarks") var remarks: String? = null,
    @SerializedName("transaction_status") var transactionStatus: String? = null

)
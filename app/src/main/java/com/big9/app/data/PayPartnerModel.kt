import com.google.gson.annotations.SerializedName

class PayPartnerModel {

    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("refid")
    var refid: Int? = null
    @SerializedName("data")
    var data: ArrayList<PayPartnerData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null
}

class PayPartnerData {

    @SerializedName("amount")
    var amount: String? = null
    @SerializedName("paymentby")
    var paymentby: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("receiveby")
    var receiveby: String? = null
    @SerializedName("lasttransactiontime")
    var lasttransactiontime: String? = null
    @SerializedName("operatorid")
    var operatorid: String? = null
}






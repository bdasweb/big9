import com.google.gson.annotations.SerializedName

class beneficiaryListModel {
    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var data: ArrayList<beneficiaryListData> = arrayListOf()
    @SerializedName("timestamp")
    var timestamp: String? = null
}

class beneficiaryListData {
    @SerializedName("rec_name")
    var recName: String? = null
    @SerializedName("rec_id")
    var recId: String? = null
    @SerializedName("rec_acno")
    var recAcno: String? = null
    @SerializedName("rec_ifsc")
    var recIfsc: String? = null
    @SerializedName("rec_bankname")
    var recBankname: String? = null
}

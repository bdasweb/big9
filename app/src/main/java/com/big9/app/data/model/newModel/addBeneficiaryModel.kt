import com.google.gson.annotations.SerializedName

class addBeneficiaryModel {
    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("ID")
    var ID: Int? = null
    @SerializedName("custID")
    var custID: String? = null
}
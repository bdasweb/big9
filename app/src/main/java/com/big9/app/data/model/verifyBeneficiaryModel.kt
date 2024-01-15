import com.google.gson.annotations.SerializedName

class verifyBeneficiaryModel {
    @SerializedName("Description"   ) var Description  : String? = null
    @SerializedName("beneName"   ) var benName  : String? = null
    @SerializedName("response_code" ) var responseCode : Int?    = null
}
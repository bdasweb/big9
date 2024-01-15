import com.google.gson.annotations.SerializedName

class electricStatelistModel {
    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var stateList: ArrayList<String> = arrayListOf()
}
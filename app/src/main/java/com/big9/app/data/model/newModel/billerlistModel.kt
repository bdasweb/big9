import com.google.gson.annotations.SerializedName

class billerlistModel {
    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("data")
    var billerData: ArrayList<billerData> = arrayListOf()
}


data class billerData(

    @SerializedName("opid") var opid: String? = null,
    @SerializedName("opname") var opname: String? = null,
    @SerializedName("opcode") var opcode: String? = null

)
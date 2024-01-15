import com.google.gson.annotations.SerializedName

class cashCollectionModel {
    @SerializedName("message")
    var message: String? = null
    @SerializedName("redirecturl")
    var redirecturl: String? = null
    @SerializedName("status")
    var status: Int? = null
}
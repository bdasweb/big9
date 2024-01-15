package com.big9.app.data.model.sample

import com.google.gson.annotations.SerializedName

class Test {
    /*@SerializedName("response")
    var response: Response? = Response()*/

    @SerializedName("name" ) var name : String? = null


}

data class Response(

    @SerializedName("data") var data: ArrayList<Data> = arrayListOf(),
    @SerializedName("status") var status: Status? = Status(),
    @SerializedName("publish") var publish: Publish? = Publish()

)

data class Publish(

    @SerializedName("version") var version: String? = null,
    @SerializedName("developer") var developer: String? = null

)

data class Status(

    @SerializedName("msg") var msg: String? = null,
    @SerializedName("action_status") var actionStatus: Boolean? = null

)


data class Data(

    @SerializedName("name") var name: String? = null

)


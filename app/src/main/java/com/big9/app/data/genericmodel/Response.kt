package com.big9.app.data.genericmodel

import com.google.gson.annotations.SerializedName

data class Response<T>(
    //@SerializedName("data") var data: T? = null,
    @SerializedName("data") var data: List<T>? = null,
    @SerializedName("status") var status: Status? = Status(),
    @SerializedName("publish") var publish: Publish? = Publish()

)




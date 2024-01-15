package com.big9.app.data.model.onBoardindPackage

import com.google.gson.annotations.SerializedName

data class StateListModel(

    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: ArrayList<StateData> = arrayListOf(),
    @SerializedName("timestamp") var timestamp: String? = null

)

data class StateData(

    @SerializedName("id") var id: String? = null,
    @SerializedName("title") var title: String? = null,
    var isSelecetd: Boolean = false
)
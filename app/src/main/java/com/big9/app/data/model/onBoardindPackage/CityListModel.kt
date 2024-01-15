package com.big9.app.data.model.onBoardindPackage

import com.google.gson.annotations.SerializedName

data class CityListModel(

    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: ArrayList<CityData> = arrayListOf(),
    @SerializedName("timestamp") var timestamp: String? = null

)

data class CityData(

    @SerializedName("district") var district: String? = null,

    var isSelecetd: Boolean = false
)
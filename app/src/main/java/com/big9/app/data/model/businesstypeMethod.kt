package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class businesstypeMethod(
    @SerializedName("Description"   ) var Description  : String?         = null,
    @SerializedName("response_code" ) var responseCode : Int?            = null,
    @SerializedName("data"          ) var data         : ArrayList<businesstypeData> = arrayListOf(),
    @SerializedName("timestamp"     ) var timestamp    : String?         = null
)

data class businesstypeData(
    @SerializedName("id"    ) var id    : String? = null,
    @SerializedName("title" ) var title : String? = null
)




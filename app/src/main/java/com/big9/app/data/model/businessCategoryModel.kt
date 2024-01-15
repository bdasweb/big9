package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class businessCategoryModel(
    @SerializedName("Description"   ) var Description  : String?         = null,
    @SerializedName("response_code" ) var responseCode : Int?            = null,
    @SerializedName("data"          ) var data         : ArrayList<businessCategoryData> = arrayListOf(),
    @SerializedName("timestamp"     ) var timestamp    : String?           = null
)


data class businessCategoryData(
    @SerializedName("id"    ) var id    : String? = null,
    @SerializedName("title" ) var title : String? = null
)






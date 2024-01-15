package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class ResetTPINModel(
    @SerializedName("Description" ) var Description : String? = null,
    @SerializedName("status"  ) var status  : Int?    = null

)



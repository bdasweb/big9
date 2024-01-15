package com.big9.app.data.genericmodel

import com.google.gson.annotations.SerializedName

data class Publish(
    @SerializedName("version") var version: String? = null,
    @SerializedName("developer") var developer: String? = null
)

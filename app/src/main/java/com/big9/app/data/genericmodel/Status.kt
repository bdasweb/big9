package com.big9.app.data.genericmodel

import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("msg") var msg: String? = null,
    @SerializedName("action_status" ) var actionStatus: Boolean? = null
)

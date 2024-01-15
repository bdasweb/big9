package com.big9.app.data.model.onBoardindPackage

import com.google.gson.annotations.SerializedName

data class BasicInfo(

    @SerializedName("userid") var userid: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("mobileno") var mobileno: String? = null,
    @SerializedName("altmobileno") var altmobileno: String? = null,
    @SerializedName("emailid") var emailid: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("pinCode") var pinCode: String? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("district") var district: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("area") var area: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("aadhaarno") var aadhaarno: String? = null

)
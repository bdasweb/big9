package com.big9.app.data.model.login

import com.google.gson.annotations.SerializedName

class LoginResponse {
    /*@SerializedName("response")
    var response: Response? = Response()*/

    @SerializedName("Auth_token")
    var AuthToken: String? = null
    @SerializedName("refresh_token")
    var refreshToken: String? = null
    @SerializedName("userid")
    var userid: String? = null
    @SerializedName("publishAt")
    var publishAt: String? = null
    @SerializedName("userstatus")
    var userstatus: String? = null
    @SerializedName("mobile_no")
    var mobileNo: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("Email")
    var Email: String? = null
    @SerializedName("UserType")
    var UserType: String? = null
    @SerializedName("before_login")
    var beforeLogin: String? = null
    @SerializedName("userStatus")
    var userStatus: String? = null
    @SerializedName("kycstep")
    var kycstep: String? = null
    @SerializedName("aadhaarno")
    var aadhar: String? = null
    @SerializedName("panno")
    var pancard: String? = null


}


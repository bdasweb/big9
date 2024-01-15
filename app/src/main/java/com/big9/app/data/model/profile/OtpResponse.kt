package com.big9.app.data.model.profile

import com.google.gson.annotations.SerializedName

class profileResponse {


    @SerializedName("Description")
    var Description: String? = null

    @SerializedName("response_code")
    var responseCode: String? = null

    @SerializedName("userid")
    var userid: String? = null

    @SerializedName("data")
    var data: Data? = Data()

    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class Data(
    @SerializedName("ID") var ID: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("mobileNo") var mobileNo: String? = null,
    @SerializedName("AlternateNumber") var AlternateNumber: String? = null,
    @SerializedName("email_id") var emailId: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("userType") var userType: String? = null,
    @SerializedName("kyc_status") var kycStatus: String? = null,
    @SerializedName("aeps_kyc_status") var aepsKycStatus: String? = null,
    @SerializedName("kyc_step") var kycStep: String? = null,
    @SerializedName("curr_balance") var currBalance: String? = null,
    @SerializedName("payout_balance") var payoutBalance: String? = null,
    @SerializedName("payabhi_wallet") var payabhiWallet: String? = null,
    @SerializedName("users_status") var usersStatus: String? = null,
    @SerializedName("userold_status") var useroldStatus: String? = null,
    @SerializedName("credo_onboarding_flag") var credoOnboardingFlag: String? = null,
    @SerializedName("matm_onbording1") var matmOnbording1: String? = null,
    @SerializedName("matm_onbording2") var matmOnbording2: String? = null,
    @SerializedName("franid") var franid: String? = null,
    @SerializedName("dealid") var dealid: String? = null,
    @SerializedName("distid") var distid: String? = null,
    @SerializedName("lienbal") var lienbal: String? = null,
    @SerializedName("lienbal_payout") var lienbalPayout: String? = null,
    @SerializedName("dealarId") var dealarId: String? = null,
    @SerializedName("dealarName") var dealarName: String? = null,
    @SerializedName("dealarMobileNo") var dealarMobileNo: String? = null,
    @SerializedName("distId") var distId: String? = null,
    @SerializedName("distName") var distName: String? = null,
    @SerializedName("distMobileNo") var distMobileNo: String? = null,
    @SerializedName("franId") var franId: String? = null,
    @SerializedName("franName") var franName: String? = null,
    @SerializedName("franMobileNo") var franMobileNo: String? = null,
    @SerializedName("aadhaar_no") var aadhaarNo: String? = null,
    @SerializedName("addressProofType") var addressProofType: String? = null,
    @SerializedName("pan_no") var panNo: String? = null,
    @SerializedName("btype") var btype: String? = null,
    @SerializedName("bname") var bname: String? = null,
    @SerializedName("bcategory") var bcategory: String? = null,
    @SerializedName("baddress") var baddress: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("area") var area: String? = null,
    @SerializedName("PanImageName") var PanImageName: String? = null,
    @SerializedName("PanImageData") var PanImageData: String? = null,
    @SerializedName("AadharFrontImageName") var AadharFrontImageName: String? = null,
    @SerializedName("AadharFrontImageData") var AadharFrontImageData: String? = null,
    @SerializedName("AadharBackImageName") var AadharBackImageName: String? = null,
    @SerializedName("AadharBackImageData") var AadharBackImageData: String? = null,
    @SerializedName("SelfieImageName") var SelfieImageName: String? = null,
    @SerializedName("SelfieImageData") var SelfieImageData: String? = null

)




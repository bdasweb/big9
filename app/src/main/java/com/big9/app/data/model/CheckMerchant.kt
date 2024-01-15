package com.big9.app.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CheckMerchant(

    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: CheckMerchantData? = CheckMerchantData()
)

data class CheckMerchantData(

    @SerializedName("userid") var userid: Int? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("pan_no") var panNo: String? = null,
    @SerializedName("mcode") var mcode: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("partnerID") var partnerID: String? = null,
    @SerializedName("partnerKey") var partnerKey: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(userid)
        parcel.writeString(pincode)
        parcel.writeString(panNo)
        parcel.writeString(mcode)
        parcel.writeString(name)
        parcel.writeString(mobile)
        parcel.writeString(email)
        parcel.writeString(partnerID)
        parcel.writeString(partnerKey)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckMerchantData> {
        override fun createFromParcel(parcel: Parcel): CheckMerchantData {
            return CheckMerchantData(parcel)
        }

        override fun newArray(size: Int): Array<CheckMerchantData?> {
            return arrayOfNulls(size)
        }
    }
}


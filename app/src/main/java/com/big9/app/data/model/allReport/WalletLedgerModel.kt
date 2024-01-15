package com.big9.app.data.model.allReport

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class WalletLedgerModel {

    @SerializedName("Description"   ) var Description  : String?         = null
    @SerializedName("response_code" ) var responseCode : Int?            = null
    @SerializedName("data"          ) var data         : ArrayList<WalletLedgerData> = arrayListOf()
    @SerializedName("timestamp"     ) var timestamp    : String?         = null



}
@Entity(tableName = "WalletLedgerdata_table")
data class WalletLedgerData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("refillid" ) var refillid : String? = null,
    @SerializedName("iid"      ) var iid      : String? = null,
    @SerializedName("insdate"  ) var insdate  : String? = null,
    @SerializedName("type"     ) var type     : String? = null,
    @SerializedName("status"   ) var status   : String? = null,
    @SerializedName("amount"   ) var amount   : String? = null,
    @SerializedName("curramt"  ) var curramt  : String? = null

)




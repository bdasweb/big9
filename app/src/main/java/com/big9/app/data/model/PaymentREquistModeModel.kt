package com.big9.app.data.model

import com.google.gson.annotations.SerializedName

data class PaymentREquistModeModel(

    @SerializedName("Description") var Description: String? = null,
    @SerializedName("response_code") var responseCode: Int? = null,
    @SerializedName("data") var data: ArrayList<PaymentREquistModeModelData> = arrayListOf(),
    @SerializedName("timestamp") var timestamp: String? = null
)

data class PaymentREquistModeModelData(

    @SerializedName("bank_id") var bankId: String? = null,
    @SerializedName("bank_name") var bankName: String? = null,
    @SerializedName("cashcounter_deposit") var cashcounterDeposit: String? = null,
    @SerializedName("cashcounter_deposit_min_amount") var cashcounterDepositMinAmount: String? = null,
    @SerializedName("cashcounter_deposit_max_amount") var cashcounterDepositMaxAmount: String? = null,
    @SerializedName("cash_cdm") var cashCdm: String? = null,
    @SerializedName("cash_cdm_min_amount") var cashCdmMinAmount: String? = null,
    @SerializedName("cash_cdm_max_amount") var cashCdmMaxAmount: String? = null,
    @SerializedName("credit") var credit: String? = null,
    @SerializedName("credit_min_amount") var creditMinAmount: String? = null,
    @SerializedName("credit_max_amount") var creditMaxAmount: String? = null,
    @SerializedName("atm") var atm: String? = null,
    @SerializedName("atm_min_amount") var atmMinAmount: String? = null,
    @SerializedName("atm_max_amount") var atmMaxAmount: String? = null,
    @SerializedName("cheque") var cheque: String? = null,
    @SerializedName("cheque_min_amount") var chequeMinAmount: String? = null,
    @SerializedName("cheque_max_amount") var chequeMaxAmount: String? = null,
    @SerializedName("online_same_bank") var onlineSameBank: String? = null,
    @SerializedName("online_same_bank_min_amount") var onlineSameBankMinAmount: String? = null,
    @SerializedName("online_same_bank_max_amount") var onlineSameBankMaxAmount: String? = null,
    @SerializedName("online_imps") var onlineImps: String? = null,
    @SerializedName("online_imps_min_amount") var onlineImpsMinAmount: String? = null,
    @SerializedName("online_imps_max_amount") var onlineImpsMaxAmount: String? = null,
    @SerializedName("online_neft") var onlineNeft: String? = null,
    @SerializedName("online_neft_min_amount") var onlineNeftMinAmount: String? = null,
    @SerializedName("online_neft_max_amount") var onlineNeftMaxAmount: String? = null,
    @SerializedName("online_rtgs") var onlineRtgs: String? = null,
    @SerializedName("online_rtgs_min_amount") var onlineRtgsMinAmount: String? = null,
    @SerializedName("online_rtgs_max_amount") var onlineRtgsMaxAmount: String? = null
)


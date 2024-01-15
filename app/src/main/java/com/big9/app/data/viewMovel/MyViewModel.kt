package com.big9.app.data.viewMovel

import AddRetailarDetailsModel
import AddretailerModel
import InsuranceModel
import MoneyTranspherModel
import PayPartnerModel
import ViewRetailerModel
import addBeneficiaryModel
import addRemitterModel
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import appUpdateUrlModel
import beneficiaryListModel
import billerlistModel
import billpaytransactionModel
import cashCollectionModel
import checkUserModel
import com.big9.app.R


import com.big9.app.data.genericmodel.BaseResponse
import com.big9.app.data.model.AEPSReportModel
import com.big9.app.data.model.AddBankBankListModel
import com.big9.app.data.model.AddBankModel
import com.big9.app.data.model.AllBankListModel
import com.big9.app.data.model.ChangeUserPasswordModel
import com.big9.app.data.model.ChangeUserTPINPasswordModel
import com.big9.app.data.model.CheckMerchant
import com.big9.app.data.model.CreditCardSendOtpModel
import com.big9.app.data.model.CreditCardVerifyOtpModel
import com.big9.app.data.model.DMTReportModel
import com.big9.app.data.model.EPotlyTranspherModel
import com.big9.app.data.model.ForgotPasswordModel
import com.big9.app.data.model.ForgotPasswordVerifyOtpModel
import com.big9.app.data.model.MatmeportModel
import com.big9.app.data.model.MoveToBankBankListModel
import com.big9.app.data.model.MoveToWalletModel
import com.big9.app.data.model.PatternLoginModel
import com.big9.app.data.model.PaymentREquistModeModel
import com.big9.app.data.model.PaymentRequistModel
import com.big9.app.data.model.PrePaidMobileOperatorListModel
import com.big9.app.data.model.PrepaidMobolePlainModel
import com.big9.app.data.model.ResetTPINModel
import com.big9.app.data.model.ServiceCheckModel
import com.big9.app.data.model.SubmitMoveToBankBankListModel
import com.big9.app.data.model.TransactionReportModel
import com.big9.app.data.model.allReport.Bank_settle_reportModel
import com.big9.app.data.model.allReport.Cashout_ledger_reportModel
import com.big9.app.data.model.allReport.DmtReportReportModel
import com.big9.app.data.model.allReport.MicroatmReportModel
import com.big9.app.data.model.allReport.PostPaidMobileOperatorListModel
import com.big9.app.data.model.allReport.TransactionReportResponse
import com.big9.app.data.model.allReport.WalletLedgerModel
import com.big9.app.data.model.allReport.WalletSettleReportModel
import com.big9.app.data.model.allReport.aepsReportModel
import com.big9.app.data.model.allReport.commissionReportModel
import com.big9.app.data.model.allReport.complaints_reportMode
import com.big9.app.data.model.allReport.loadRequestModel
import com.big9.app.data.model.login.LoginResponse
import com.big9.app.data.model.onBoardindPackage.BasicInfo
import com.big9.app.data.model.onBoardindPackage.CityListModel
import com.big9.app.data.model.onBoardindPackage.StateListModel
import com.big9.app.data.model.otp.OtpResponse
import com.big9.app.data.model.paymentReport.PaymentReportResponse
import com.big9.app.data.model.profile.profileResponse
import com.big9.app.network.ResponseState

import com.big9.app.repository.AuthRepositoryRepository
import com.big9.app.repository.TableRepository

import com.big9.app.utils.helpers.helper.validate


import dagger.hilt.android.lifecycle.HiltViewModel
import electricBillbillFetchModel
import electricStatelistModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import refreshTokenModel
import verifyBeneficiaryModel
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: AuthRepositoryRepository,
    private val tableRepository: TableRepository
) : ViewModel() {
    var fromPage = ""
    var data: Map<String, Any?>? = null
    val selectedButton = MutableLiveData<Int>()
    var paySleeyUri: String? = ""
    var denomSlipUri: String? = ""

    var paySleeyUriUri: Uri? = null
    var denomSlipUriUri: Uri? = null


    val userProfileMobile = MutableLiveData<String>()
    val userProfileName = MutableLiveData<String>()

    val from_page_message = MutableLiveData<String>()

    val popup_message = MutableLiveData<String>("Success!")
    val receiveStatus = MutableLiveData<String>("")
    val receiveReceptMessahe = MutableLiveData<String>("")
    val loginPin = MutableLiveData<String>("")
    val otp: MutableLiveData<String> = MutableLiveData("")
    var otpMobile = MutableLiveData<String>("")
    var sendMoneyVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    var filePath: MutableLiveData<Uri> = MutableLiveData()
    val selectedBank = MutableLiveData<String>("")
    val selectedBankId = MutableLiveData<String>("")
    val selectedBankMode = MutableLiveData<String>("")
    val minMaxBalence = MutableLiveData<String>("")
    val prepaitOrPostPaid = MutableLiveData<String>("")
    val reportType = MutableLiveData<String>("")
    val reportTypeIDRecept = MutableLiveData<String>("")
    val startDate = MutableLiveData<String>("")
    val enddate = MutableLiveData<String>("")
    val kycStatus = MutableLiveData<String>("KYC Approved")

    val bankSlipDocumentImageBase64 = MutableLiveData<String>()

    val tPin = MutableLiveData<String>("")
    val state = MutableLiveData<String>()
    val district = MutableLiveData<String>()

    val billerAddress = MutableLiveData<String>()

    val userName = MutableLiveData<String>()
    val balence = MutableLiveData<String>()
    val nextRecharge = MutableLiveData<String>()
    val monthly = MutableLiveData<String>()
    val type = MutableLiveData<String>()


    val prepaitIsActive = MutableLiveData<Boolean>()
    val postpaitIsActive = MutableLiveData<Boolean>()

    val IMPSIsActive = MutableLiveData<Boolean>()
    val NEFTIsActive = MutableLiveData<Boolean>()


    val operatorName = MutableLiveData<String>()

    val minMobileLength = MutableLiveData<Int>()
    val maxMobileLength = MutableLiveData<Int>()

    val minrecharge = MutableLiveData<Int>()
    val maxrecharge = MutableLiveData<Int>()

    //For creditcard
    val creditCardID = MutableLiveData<String>()
    val stateresp = MutableLiveData<String>()


    val ratailerPayAmt = MutableLiveData<String>()
    val ratailerMobile = MutableLiveData<String>()
    val mobile = MutableLiveData<String>()
    val operator = MutableLiveData<String>()
    val operatorCode = MutableLiveData<String>()
    val amt = MutableLiveData<String>()
    val subId = MutableLiveData<String>()
    val dthOperator = MutableLiveData<String>()
    val selectrdOperator = MutableLiveData<String>()
    val selectrdOperatorForRecept = MutableLiveData<String>()
    val selectrdBroadbandOperator = MutableLiveData<String>()
    val dthAmt = MutableLiveData<String>()

    val consumerId = MutableLiveData<String>()
    val consumerIdPrice = MutableLiveData<String>()
    val oldPin = MutableLiveData<String>()
    val newPin = MutableLiveData<String>()
    val confirmPin = MutableLiveData<String>()

    val oldTPin = MutableLiveData<String>()
    val newTPin = MutableLiveData<String>()
    val confirmTPin = MutableLiveData<String>()
    val epotly_mobile = MutableLiveData<String>()
    val epotly_amt = MutableLiveData<String>()
    val cash_withdraw_pin_code = MutableLiveData<String>()
    val cash_withdraw_pan = MutableLiveData<String>()
    val paymentAmt = MutableLiveData<String>()
    val transActionId = MutableLiveData<String>()
    val particular = MutableLiveData<String>()
    val amtMoveToWallet = MutableLiveData<String>()
    val amtMoveToPayabhi = MutableLiveData<String>()


    val denomination_10 = MutableLiveData<String>("0")
    val denomination_20 = MutableLiveData<String>("0")
    val denomination_50 = MutableLiveData<String>("0")
    val denomination_100 = MutableLiveData<String>("0")
    val denomination_200 = MutableLiveData<String>("0")
    val denomination_500 = MutableLiveData<String>("0")
    val denomination_2000 = MutableLiveData<String>("0")

    val amtMoveToBank = MutableLiveData<String>()
    val mobileSendMoney = MutableLiveData<String>()
    val nameSendMoney = MutableLiveData<String>()

    val credit_card = MutableLiveData<String>()
    val credit_holder_name = MutableLiveData<String>()
    val credit_remarks = MutableLiveData<String>()
    val select_card_type = MutableLiveData<String>()
    val credit_mobile = MutableLiveData<String>()
    val credit_amt = MutableLiveData<String>()


    val beneficiary_bank_name = MutableLiveData<String>()
    val beneficiary_ifsc = MutableLiveData<String>()
    val bankIdBene = MutableLiveData<String>()
    val beneficiary_acc = MutableLiveData<String>()
    val beneficiary_name = MutableLiveData<String>()


    val provided_amt = MutableLiveData<String>()
    val provided_aadhar_number = MutableLiveData<String>()
    val provided_customer_number = MutableLiveData<String>()


    val ratailerName = MutableLiveData<String>()
    val ratailerShopName = MutableLiveData<String>()
    val ratailerEmail = MutableLiveData<String>()
    val ratailerAddress = MutableLiveData<String>()

    val vehicleRegId = MutableLiveData<String>()
    val fastTagOperator = MutableLiveData<String>()
    val fastTagAmt = MutableLiveData<String>()

    val balence_enquary_aadhar_number = MutableLiveData<String>()
    val balence_enquary_customer_number = MutableLiveData<String>()

    val selectBank = MutableLiveData<String>()
    val loanAccountNumber = MutableLiveData<String>()

    val selectOperator = MutableLiveData<String>()
    val selectImage = MutableLiveData<Int>()

    val operatorSubId = MutableLiveData<String>()
    val operatordthAmt = MutableLiveData<String>()

    val instituteName = MutableLiveData<String>()
    val selectInstitute = MutableLiveData<String>()
    val educationAmt = MutableLiveData<String>()
    val loanAmount = MutableLiveData<String>()
    val education_bank_name = MutableLiveData<String>()
    val education_bank_ifsc = MutableLiveData<String>()


    val gasbookingNumber = MutableLiveData<String>()
    val gasBiller = MutableLiveData<String>()
    val gasBookingAmt = MutableLiveData<String>()


    val ratailerMobileMobileError = MutableLiveData<String>()
    val mobileError = MutableLiveData<String>()
    val operatorError = MutableLiveData<String>()
    val amtError = MutableLiveData<String>()
    val subIdError = MutableLiveData<String>()
    val dthError = MutableLiveData<String>()
    val dthAmtError = MutableLiveData<String>()

    val epotly_amt_Error = MutableLiveData<String>()

    val consumerIdError = MutableLiveData<String>()
    val consumerIdPriceError = MutableLiveData<String>()
    val oldPinError = MutableLiveData<String>()
    val newPinError = MutableLiveData<String>()
    val confirmPinError = MutableLiveData<String>()

    val oldTPinError = MutableLiveData<String>()
    val newTPinError = MutableLiveData<String>()
    val confirmTPinError = MutableLiveData<String>()
    val epotly_mobileError = MutableLiveData<String>()
    val cash_withdraw_pin_codeError = MutableLiveData<String>()
    val cash_withdraw_pan_codeError = MutableLiveData<String>()



    val ratailerNameError = MutableLiveData<String>()
    val ratailerShopNameError = MutableLiveData<String>()
    val ratailerEmailError = MutableLiveData<String>()
    val ratailerAddressError = MutableLiveData<String>()

    val paymentAmtError = MutableLiveData<String>()
    val depositeDate = MutableLiveData<String>()
    val depositeDateError = MutableLiveData<String>()
    val particularError = MutableLiveData<String>()
    val transActionIdError = MutableLiveData<String>()
    val amtMoveToWalletError = MutableLiveData<String>()
    val amtMoveToPayabhiError = MutableLiveData<String>()
    val amtMoveToBankError = MutableLiveData<String>()
    val amtMoveToBankModeError = MutableLiveData<String>()
    val mobileSendMoneyError = MutableLiveData<String>()
    val nameSendMoneyError = MutableLiveData<String>()

    val credit_cardError = MutableLiveData<String>()
    val credit_holder_name_Error = MutableLiveData<String>()
    val credit_remarks_Error = MutableLiveData<String>()
    val credit_mobile_Error = MutableLiveData<String>()
    val credit_amt_Error = MutableLiveData<String>()

    val beneficiary_bank_name_Error = MutableLiveData<String>()
    val beneficiary_ifsc_Error = MutableLiveData<String>()
    val beneficiary_accError = MutableLiveData<String>()
    val beneficiary_nameError = MutableLiveData<String>()

    val provided_amt_Error = MutableLiveData<String>()
    val provided_aadhar_numberError = MutableLiveData<String>()
    val provided_customer_numberError = MutableLiveData<String>()

    val fastTagOperatorError = MutableLiveData<String>()
    val vehicleRegIdError = MutableLiveData<String>()
    val fastTagAmtError = MutableLiveData<String>()

    val balence_enquary_aadhar_numberError = MutableLiveData<String>()
    val balence_enquary_customer_numberError = MutableLiveData<String>()

    val selectBankError = MutableLiveData<String>()
    val loanAccountNumberError = MutableLiveData<String>()
    val loanAmountError = MutableLiveData<String>()


    val selectOperatorError = MutableLiveData<String>()
    val operatorSubIdError = MutableLiveData<String>()
    val operatordthAmtError = MutableLiveData<String>()

    val instituteNameError = MutableLiveData<String>()
    val selectInstituteError = MutableLiveData<String>()
    val education_bank_nameError = MutableLiveData<String>()
    val educationAmtError = MutableLiveData<String>()


    val gasbookingNumberError = MutableLiveData<String>()
    val gasBillerError = MutableLiveData<String>()
    val gasBookingAmtError = MutableLiveData<String>()


    val ratailerMobileMobileErrorVisible = MutableLiveData<Boolean>()
    val mobileErrorVisible = MutableLiveData<Boolean>()
    val operatorErrorVisible = MutableLiveData<Boolean>()
    val amtErrorErrorVisible = MutableLiveData<Boolean>()
    val subIdErrorVisible = MutableLiveData<Boolean>()
    val dthErrorErrorVisible = MutableLiveData<Boolean>()
    val dthAmtErrorVisible = MutableLiveData<Boolean>()


    val ratailerNameErrorVisible = MutableLiveData<Boolean>()
    val ratailerShopNameErrorVisible = MutableLiveData<Boolean>()
    val ratailerEmailErrorVisible = MutableLiveData<Boolean>()
    val ratailerAddressErrorVisible = MutableLiveData<Boolean>()


    val consumerIdErrorVisible = MutableLiveData<Boolean>()
    val consumerIdPriceErrorVisible = MutableLiveData<Boolean>()
    val oldPinErrorVisible = MutableLiveData<Boolean>()
    val newPinErrorVisible = MutableLiveData<Boolean>()
    val confirmPinErrorVisible = MutableLiveData<Boolean>()

    val oldTPinErrorVisible = MutableLiveData<Boolean>()
    val newTPinErrorVisible = MutableLiveData<Boolean>()
    val confirmTPinErrorVisible = MutableLiveData<Boolean>()
    val epotly_mobileErrorVisible = MutableLiveData<Boolean>()
    val epotly_amt_ErrorVisible = MutableLiveData<Boolean>()
    val cash_withdraw_pin_codeErrorVisible = MutableLiveData<Boolean>()
    val cash_withdraw_pan_codeErrorVisible = MutableLiveData<Boolean>()

    val paymentAmtErrorVisible = MutableLiveData<Boolean>()
    val depositeDateErrorVisible = MutableLiveData<Boolean>()
    val particularErrorVisible = MutableLiveData<Boolean>()
    val transActionIdErrorVisible = MutableLiveData<Boolean>()
    val amtMoveToWalletErrorVisible = MutableLiveData<Boolean>()
    val amtMoveToPayabhiErrorVisible = MutableLiveData<Boolean>()
    val amtMoveToBankErrorVisible = MutableLiveData<Boolean>()
    val bankModeMoveToBankErrorVisible = MutableLiveData<Boolean>()
    val mobileSendMoneyErrorVisible = MutableLiveData<Boolean>()
    val nameSendMoneyErrorVisible = MutableLiveData<Boolean>()

    val credit_cardErrorVisible = MutableLiveData<Boolean>()
    val credit_holder_name_ErrorVisible = MutableLiveData<Boolean>()
    val credit_remarks_ErrorVisible = MutableLiveData<Boolean>()
    var select_card_type_ErrorVisible = MutableLiveData<Boolean>()
    val credit_mobileErrorVisible = MutableLiveData<Boolean>()
    val credit_amt_ErrorVisible = MutableLiveData<Boolean>()

    val beneficiary_bank_name_ErrorVisible = MutableLiveData<Boolean>()
    val beneficiary_ifsc_ErrorVisible = MutableLiveData<Boolean>()
    val beneficiary_accErrorVisible = MutableLiveData<Boolean>()
    val beneficiary_nameErrorVisible = MutableLiveData<Boolean>()

    val provided_amt_ErrorVisible = MutableLiveData<Boolean>()
    val provided_aadhar_numberErrorVisible = MutableLiveData<Boolean>()
    val provided_customer_numberErrorVisible = MutableLiveData<Boolean>()

    val vehicleRegIdErrorVisible = MutableLiveData<Boolean>()
    val fastTagOperatorErrorVisible = MutableLiveData<Boolean>()
    val fastTagAmtErrorVisible = MutableLiveData<Boolean>()

    val bank_check_ErrorVisible = MutableLiveData<Boolean>()


    val balence_enquary_aadhar_numberErrorVisible = MutableLiveData<Boolean>()
    val balence_enquary_customer_numberErrorVisible = MutableLiveData<Boolean>()

    val selectBankErrorVisible = MutableLiveData<Boolean>()
    val loanAccountNumberErrorVisible = MutableLiveData<Boolean>()
    val loanAmountErrorVisible = MutableLiveData<Boolean>()

    val selectOperatorErrorVisible = MutableLiveData<Boolean>()
    val operatorSubIdErrorVisible = MutableLiveData<Boolean>()
    val operatordthAmtErrorVisible = MutableLiveData<Boolean>()

    val instituteNameErrorVisible = MutableLiveData<Boolean>()
    val education_bank_nameErrorVisible = MutableLiveData<Boolean>()
    val selectInstituteErrorVisible = MutableLiveData<Boolean>()
    val educationAmtErrorVisible = MutableLiveData<Boolean>()


    val gasbookingNumberErrorVisible = MutableLiveData<Boolean>()
    val gasBillerErrorVisible = MutableLiveData<Boolean>()
    val gasBookingAmtErrorVisible = MutableLiveData<Boolean>()

    fun regValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (mobile.value?.trim().isNullOrBlank()) {
            mobileError.value = "Mobile number is required"
            mobileErrorVisible.value = true
            isValid = false
        } else {
            if ((mobile?.value?.length ?: -1 >= minMobileLength?.value ?: 0) && (mobile?.value?.length ?: -1 <= maxMobileLength?.value ?: 0)) {
                mobileError.value = ""
                mobileErrorVisible.value = false
            } else {
                mobileError.value = "Valid mobile number required"
                mobileErrorVisible.value = true
                isValid = false
            }

        }

        if (amt.value?.trim().isNullOrBlank()) {
            amtError.value = "Please enter amount"
            amtErrorErrorVisible.value = true
            isValid = false
        } else {
            try {
                val amtValue = amt?.value?.toInt()
                if ((amtValue ?: -1 >= minrecharge?.value ?: 0) && (amtValue ?: -1 <= maxrecharge?.value ?: 0)) {
                    amtError.value = ""
                    amtErrorErrorVisible.value = false
                } else {
                    amtError.value =
                        "Kindly provide a valid amount. Ensure the amount is not less than ₹${minrecharge?.value} and does not exceed ₹${maxrecharge?.value}"
                    amtErrorErrorVisible.value = true
                    isValid = false
                }
            } catch (e: Exception) {
                amtError.value = "Please enter valid amount"
                amtErrorErrorVisible.value = true
                isValid = false
            }


        }

        if (operator.value?.trim().isNullOrBlank()) {
            operatorError.value = "Please select operator"
            operatorErrorVisible.value = true
            isValid = false
        } else {
            operatorError.value = ""
            operatorErrorVisible.value = false
        }



        return isValid
    }

    fun mobileValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (mobile.value?.trim().isNullOrBlank()) {
            mobileError.value = "Mobile number is required"
            mobileErrorVisible.value = true
            isValid = false
        } else {
            if ((mobile?.value?.length ?: -1 >= minMobileLength?.value ?: 0) && (mobile?.value?.length ?: -1 <= maxMobileLength?.value ?: 0)) {
                mobileError.value = ""
                mobileErrorVisible.value = false
            } else {
                mobileError.value = "Valid mobile number required"
                mobileErrorVisible.value = true
                isValid = false
            }

        }



        return isValid
    }


    fun dthValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (subId.value?.trim().isNullOrBlank()) {
            subIdError.value = "Please enter a valid DTH subscriber ID."
            subIdErrorVisible.value = true
            isValid = false
        } else {
            subIdError.value = ""
            subIdErrorVisible.value = false
        }

        if (dthOperator.value?.trim().isNullOrBlank()) {
            dthError.value = "Please select an operator"
            dthErrorErrorVisible.value = true
            isValid = false
        } else {
            dthError.value = ""
            dthErrorErrorVisible.value = false
        }

        if (dthAmt.value?.trim().isNullOrBlank()) {
            dthAmtError.value = "Please enter a valid amount."
            dthAmtErrorVisible.value = true
            isValid = false
        } else {
            dthAmtError.value = ""
            dthAmtErrorVisible.value = false
        }



        return isValid
    }


    fun electricValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (consumerId.value?.trim().isNullOrBlank()) {
            consumerIdError.value = "Please enter a valid Customer Id fixed 11 digit"
            consumerIdErrorVisible.value = true
            isValid = false
        } else {
            consumerIdError.value = ""
            consumerIdErrorVisible.value = false
        }

        if (consumerIdPrice.value?.trim().isNullOrBlank()) {
            consumerIdPriceError.value = "Please select amount"
            consumerIdPriceErrorVisible.value = true
            isValid = false
        } else {
            consumerIdPriceError.value = ""
            consumerIdPriceErrorVisible.value = false
        }




        return isValid
    }

    fun electricVerifyValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (consumerId.value?.trim().isNullOrBlank()) {
            consumerIdError.value = "Please enter a valid Customer Id fixed 11 digit"
            consumerIdErrorVisible.value = true
            isValid = false
        } else {
            consumerIdError.value = ""
            consumerIdErrorVisible.value = false
        }

        return isValid
    }


    /*   val loginResponseLiveData: LiveData<ResponseState<BaseResponse<Test>>>
       get() = repository.loginResponseLiveData
   fun login(requestBody: JsonObject) {
       viewModelScope.launch {
           repository.login(requestBody)
       }
   }*/


    fun changeLoginPinValidation(): Boolean {
        invisibleErrorTexts()

        var isValid = true

        // Validate Old Password
        if (oldPin.value?.trim().isNullOrBlank()) {
            oldPinError.value = "Please enter the Old PIN"
            oldPinErrorVisible.value = true
            isValid = false
        } else {
            oldPinErrorVisible.value = false
        }

        // Validate New Password
        if (newPin.value?.trim().isNullOrBlank()) {
            newPinError.value = "Please enter the New PIN"
            newPinErrorVisible.value = true
            isValid = false
        } else {
            newPinErrorVisible.value = false
        }

        // Validate Confirm Password
        if (confirmPin.value?.trim().isNullOrBlank()) {
            confirmPinError.value = "Please enter the Confirm PIN"
            confirmPinErrorVisible.value = true
            isValid = false
        } else if (confirmPin.value != newPin.value) {
            confirmPinError.value = "PINs do not match"
            confirmPinErrorVisible.value = true
            isValid = false
        } else {
            confirmPinErrorVisible.value = false
        }

        return isValid
    }

    fun changeLoginTPinValidation(): Boolean {
        invisibleErrorTexts()

        var isValid = true

        // Validate Old Password
        if (oldTPin.value?.trim().isNullOrBlank()) {
            oldTPinError.value = "Please enter the Old TPIN"
            oldTPinErrorVisible.value = true
            isValid = false
        } else {
            oldTPinErrorVisible.value = false
        }

        // Validate New Password
        if (newTPin.value?.trim().isNullOrBlank()) {
            newTPinError.value = "Please enter the New TPIN"
            newTPinErrorVisible.value = true
            isValid = false
        } else {
            newTPinErrorVisible.value = false
        }

        // Validate Confirm Password
        if (confirmTPin.value?.trim().isNullOrBlank()) {
            confirmTPinError.value = "Please enter the Confirm TPIN"
            confirmTPinErrorVisible.value = true
            isValid = false
        } else if (confirmTPin.value != newTPin.value) {
            confirmTPinError.value = "TPIN do not match"
            confirmTPinErrorVisible.value = true
            isValid = false
        } else {
            confirmTPinErrorVisible.value = false
        }

        return isValid
    }


    fun epotlyValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (epotly_mobile.value?.trim().isNullOrBlank()) {
            epotly_mobileError.value = "Mobile number is required"
            epotly_mobileErrorVisible.value = true
            isValid = false
        } else {
            if (epotly_mobile.value?.trim()?.validate("mobile") == false) {
                epotly_mobileError.value = "Mobile number is not valid"
                epotly_mobileErrorVisible.value = true
                isValid = false
            } else {
                epotly_mobileError.value = ""
                epotly_mobileErrorVisible.value = false
            }
        }

        if (epotly_amt.value?.trim().isNullOrBlank()) {
            epotly_amt_Error.value = "Please enter amount"
            epotly_amt_ErrorVisible.value = true
            isValid = false
        } else {
            epotly_amt_Error.value = ""
            epotly_amt_ErrorVisible.value = false
        }



        return isValid
    }


    fun cashWithdrawValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (cash_withdraw_pin_code.value?.trim().isNullOrBlank()) {
            cash_withdraw_pin_codeError.value = "Pin code is required"
            cash_withdraw_pin_codeErrorVisible.value = true
            isValid = false
        } else {
            if (cash_withdraw_pin_code.value?.trim()?.validate("pincode") == false) {
                cash_withdraw_pin_codeError.value = "PIN code is not valid"
                cash_withdraw_pin_codeErrorVisible.value = true
                isValid = false
            } else {
                cash_withdraw_pin_codeError.value = ""
                cash_withdraw_pin_codeErrorVisible.value = false
            }
        }



        if (cash_withdraw_pan.value?.trim().isNullOrBlank()) {
            cash_withdraw_pan_codeError.value = "Pan card number is required"
            cash_withdraw_pan_codeErrorVisible.value = true
            isValid = false
        } else {
            if (cash_withdraw_pan.value?.trim()?.validate("pan") == false) {
                cash_withdraw_pan_codeError.value = "Pan card number is required"
                cash_withdraw_pan_codeErrorVisible.value = true
                isValid = false
            } else {
                cash_withdraw_pan_codeError.value = ""
                cash_withdraw_pan_codeErrorVisible.value = false
            }
        }
        return isValid
    }


    fun PaymentrequestValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (paymentAmt.value?.trim().isNullOrBlank()) {
            paymentAmtError.value = "This field is required"
            paymentAmtErrorVisible.value = true
            isValid = false
        } else {
            paymentAmtError.value = ""
            paymentAmtErrorVisible.value = false
        }




        if (depositeDate.value?.trim().isNullOrBlank()) {
            depositeDateError.value = "This field is required"
            depositeDateErrorVisible.value = true
            isValid = false
        } else {
            depositeDateError.value = ""
            depositeDateErrorVisible.value = false
        }

        if (particular.value?.trim().isNullOrBlank()) {
            particularError.value = "This field is required"
            particularErrorVisible.value = true
            isValid = false
        } else {
            particularError.value = ""
            particularErrorVisible.value = false
        }

        if (transActionId.value?.trim().isNullOrBlank()) {
            transActionIdError.value = "This field is required"
            transActionIdErrorVisible.value = true
            isValid = false
        } else {
            transActionIdError.value = ""
            transActionIdErrorVisible.value = false
        }



        return isValid
    }

    fun settleWalletValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (amtMoveToWallet.value?.trim().isNullOrBlank()) {
            amtMoveToWalletError.value = "This field is required"
            amtMoveToWalletErrorVisible.value = true
            isValid = false
        } else {
            amtMoveToWalletError.value = ""
            amtMoveToWalletErrorVisible.value = false
        }


        return isValid
    }

    fun payabhiValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (amtMoveToPayabhi.value?.trim().isNullOrBlank()) {
            amtMoveToPayabhiError.value = "This field is required"
            amtMoveToPayabhiErrorVisible.value = true
            isValid = false
        } else {
            amtMoveToPayabhiError.value = ""
            amtMoveToPayabhiErrorVisible.value = false
        }


        return isValid
    }


    fun MoveToBankValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (amtMoveToBank.value?.trim().isNullOrBlank()) {
            amtMoveToBankError.value = "This field is required"
            amtMoveToBankErrorVisible.value = true
            isValid = false
        } else {
            amtMoveToBankError.value = ""
            amtMoveToBankErrorVisible.value = false
        }

        if (IMPSIsActive?.value == false && NEFTIsActive?.value == false) {
            isValid = false
            amtMoveToBankModeError.value = "Please select a valid bank transaction mode."
            bankModeMoveToBankErrorVisible.value = true
        } else {

            amtMoveToBankModeError.value = ""
            bankModeMoveToBankErrorVisible.value = false
        }


        return isValid
    }

    fun MoneyTranspherValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true


        if (mobileSendMoney.value?.trim().isNullOrBlank()) {
            mobileSendMoneyError.value = "Mobile number is required"
            mobileSendMoneyErrorVisible.value = true
            isValid = false
        } else {
            if (mobileSendMoney.value?.trim()?.validate("mobile") == false) {
                mobileSendMoneyError.value = "Mobile number is not valid"
                mobileSendMoneyErrorVisible.value = true
                isValid = false
            } else {
                mobileSendMoneyError.value = ""
                mobileSendMoneyErrorVisible.value = false
            }
        }
        if (sendMoneyVisibility?.value == true) {
            if (nameSendMoney.value?.trim().isNullOrBlank()) {
                nameSendMoneyError.value = "Name is required"
                nameSendMoneyErrorVisible.value = true
                isValid = false
            } else {
                if (nameSendMoney.value?.trim()?.validate("name") == false) {
                    nameSendMoneyError.value = "Name is not valid"
                    nameSendMoneyErrorVisible.value = true
                    isValid = false
                } else {
                    nameSendMoneyError.value = ""
                    nameSendMoneyErrorVisible.value = false
                }
            }
        }
        return isValid
    }

    fun MoneyTranspherValidation2(): Boolean {

        invisibleErrorTexts()

        var isValid = true


        if (mobileSendMoney.value?.trim().isNullOrBlank()) {
            mobileSendMoneyError.value = "Mobile number is required"
            mobileSendMoneyErrorVisible.value = true
            isValid = false
        } else {
            if (mobileSendMoney.value?.trim()?.validate("mobile") == false) {
                mobileSendMoneyError.value = "Mobile number is not valid"
                mobileSendMoneyErrorVisible.value = true
                isValid = false
            } else {
                mobileSendMoneyError.value = ""
                mobileSendMoneyErrorVisible.value = false
            }
        }

            if (nameSendMoney.value?.trim().isNullOrBlank()) {
                nameSendMoneyError.value = "Name is required"
                nameSendMoneyErrorVisible.value = true
                isValid = false
            } else {
                if (nameSendMoney.value?.trim()?.validate("name") == false) {
                    nameSendMoneyError.value = "Name is not valid"
                    nameSendMoneyErrorVisible.value = true
                    isValid = false
                } else {
                    nameSendMoneyError.value = ""
                    nameSendMoneyErrorVisible.value = false
                }
            }

        return isValid
    }




    fun mobileNumberValidation(): Boolean {
        invisibleErrorTexts()
        var isValid = true
        if (mobileSendMoney.value?.trim().isNullOrBlank()) {
            isValid = false
        } else {
            isValid = mobile.value?.trim()?.length == 10
        }
        return isValid
    }


    fun creditValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (credit_card.value?.trim().isNullOrBlank()) {
            credit_cardError.value = "Please enter Credit card number"
            credit_cardErrorVisible.value = true
            isValid = false
        } else {
            if (credit_card.value?.trim()?.length != 19) {
                credit_cardError.value = "Credit card number is not valid."
                credit_cardErrorVisible.value = true
            } else {
                credit_cardError.value = ""
                credit_cardErrorVisible.value = false
            }

        }



        if (credit_holder_name.value?.trim().isNullOrBlank()) {
            credit_holder_name_Error.value = "Name is required"
            credit_holder_name_ErrorVisible.value = true
            isValid = false
        } else {
            if (credit_holder_name.value?.trim()?.validate("name") == false) {
                credit_holder_name_Error.value = "Name is not valid"
                credit_holder_name_ErrorVisible.value = true
                isValid = false
            } else {
                credit_holder_name_Error.value = ""
                credit_holder_name_ErrorVisible.value = false
            }
        }


        if (credit_mobile.value?.trim().isNullOrBlank()) {
            credit_mobile_Error.value = "Mobile number is required"
            credit_mobileErrorVisible.value = true
            isValid = false
        } else {
            if (credit_mobile.value?.trim()?.validate("mobile") == false) {
                credit_mobile_Error.value = "Mobile number is not valid"
                credit_mobileErrorVisible.value = true
                isValid = false
            } else {
                credit_mobile_Error.value = ""
                credit_mobileErrorVisible.value = false
            }
        }

        if (credit_amt.value?.trim().isNullOrBlank()) {
            credit_amt_Error.value = "Please enter amount"
            credit_amt_ErrorVisible.value = true
            isValid = false
        } else {
            credit_amt_Error.value = ""
            credit_amt_ErrorVisible.value = false
        }


        if (credit_remarks.value?.trim().isNullOrBlank()) {
            credit_remarks_Error.value = "Please enter a relevant message."
            credit_remarks_ErrorVisible.value = true
            isValid = false
        } else {
            credit_remarks_Error.value = ""
            credit_remarks_ErrorVisible.value = false
        }
        if (selectedButton.value == 0) {
            isValid = false
            select_card_type_ErrorVisible.value = true
        } else {
            select_card_type_ErrorVisible.value = false
            select_card_type.value = when (selectedButton.value) {
                R.id.radioVisa -> {
                    "visa"
                }

                R.id.radioMasterCard -> {
                    "mastercard"
                }

                else -> {
                    "amex"
                }
            }

        }


        return isValid
    }

    fun beneficiaryValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true


        if (beneficiary_bank_name.value?.trim().isNullOrBlank()) {
            beneficiary_bank_name_Error.value = "This field is required"
            beneficiary_bank_name_ErrorVisible.value = true
            isValid = false
        } else {
            beneficiary_bank_name_Error.value = ""
            beneficiary_bank_name_ErrorVisible.value = false

        }



        if (beneficiary_ifsc.value?.trim().isNullOrBlank()) {
            beneficiary_ifsc_Error.value = "This field is required"
            beneficiary_ifsc_ErrorVisible.value = true
            isValid = false
        } else {
            beneficiary_ifsc_Error.value = ""
            beneficiary_ifsc_ErrorVisible.value = false
        }


        if (beneficiary_acc.value?.trim().isNullOrBlank()) {
            beneficiary_accError.value = "This field is required"
            beneficiary_accErrorVisible.value = true
            isValid = false
        } else {
            beneficiary_accError.value = ""
            beneficiary_accErrorVisible.value = false
        }

        if (beneficiary_name.value?.trim().isNullOrBlank()) {
            beneficiary_nameError.value = "This field is required"
            beneficiary_nameErrorVisible.value = true
            isValid = false
        } else {

            if (beneficiary_name.value?.trim()?.validate("name")==false) {
                beneficiary_nameError.value = "Name is not valid"
                beneficiary_nameErrorVisible.value = true
                isValid = false
            }
            else {

                beneficiary_nameError.value = ""
                beneficiary_nameErrorVisible.value = false
            }
        }
        return isValid
    }

    fun beneficiaryVerifyValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true


        if (beneficiary_bank_name.value?.trim().isNullOrBlank()) {
            beneficiary_bank_name_Error.value = "This field is required"
            beneficiary_bank_name_ErrorVisible.value = true
            isValid = false
        } else {
            beneficiary_bank_name_Error.value = ""
            beneficiary_bank_name_ErrorVisible.value = false

        }



        if (beneficiary_ifsc.value?.trim().isNullOrBlank()) {
            beneficiary_ifsc_Error.value = "This field is required"
            beneficiary_ifsc_ErrorVisible.value = true
            isValid = false
        } else {
            beneficiary_ifsc_Error.value = ""
            beneficiary_ifsc_ErrorVisible.value = false
        }


        if (beneficiary_acc.value?.trim().isNullOrBlank()) {
            beneficiary_accError.value = "This field is required"
            beneficiary_accErrorVisible.value = true
            isValid = false
        } else {
            beneficiary_accError.value = ""
            beneficiary_accErrorVisible.value = false
        }


        return isValid
    }

    fun cashWithdrawalValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true


        if (provided_amt.value?.trim().isNullOrBlank()) {
            provided_amt_Error.value = "This field is required"
            provided_amt_ErrorVisible.value = true
            isValid = false
        } else {
            provided_amt_Error.value = ""
            provided_amt_ErrorVisible.value = false

        }



        if (provided_aadhar_number.value?.trim().isNullOrBlank()) {
            provided_aadhar_numberError.value = "Aadhar number is required"
            provided_aadhar_numberErrorVisible.value = true
            isValid = false
        } else {
            if (provided_aadhar_number.value?.trim()?.validate("aadhar") == false) {
                provided_aadhar_numberError.value = "Aadhar number is not valid"
                provided_aadhar_numberErrorVisible.value = true
                isValid = false
            } else {
                provided_aadhar_numberError.value = ""
                provided_aadhar_numberErrorVisible.value = false
            }
        }


        if (provided_customer_number.value?.trim().isNullOrBlank()) {
            provided_customer_numberError.value = "This field is required"
            provided_customer_numberErrorVisible.value = true
            isValid = false
        } else {
            provided_customer_numberError.value = ""
            provided_customer_numberErrorVisible.value = false
        }







        return isValid
    }

    fun fastTagValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (vehicleRegId.value?.trim().isNullOrBlank()) {
            vehicleRegIdError.value = "Registration Number"
            vehicleRegIdErrorVisible.value = true
            isValid = false
        } else {


            if (vehicleRegId.value?.trim()?.validate("vehicle") == false) {
                vehicleRegIdError.value = "Invalid Registration Number"
                vehicleRegIdErrorVisible.value = true
                isValid = false
            } else {
                vehicleRegIdError.value = ""
                vehicleRegIdErrorVisible.value = false
            }
        }

        if (fastTagOperator.value?.trim().isNullOrBlank()) {
            fastTagOperatorError.value = "Please select an operator"
            fastTagOperatorErrorVisible.value = true
            isValid = false
        } else {
            fastTagOperatorError.value = ""
            fastTagOperatorErrorVisible.value = false
        }

        if (fastTagAmt.value?.trim().isNullOrBlank()) {
            fastTagAmtError.value = "Please enter a valid amount."
            fastTagAmtErrorVisible.value = true
            isValid = false
        } else {
            fastTagAmtError.value = ""
            fastTagAmtErrorVisible.value = false
        }
        return isValid
    }

    fun balenceValidation(): Boolean {
        invisibleErrorTexts()

        var isValid = true
        if (balence_enquary_aadhar_number.value?.trim().isNullOrBlank()) {
            balence_enquary_aadhar_numberError.value = "Aadhar number is required"
            balence_enquary_aadhar_numberErrorVisible.value = true
            isValid = false
        } else {
            if (balence_enquary_aadhar_number.value?.trim()?.validate("aadhar") == false) {
                balence_enquary_aadhar_numberError.value = "Aadhar number is not valid"
                balence_enquary_aadhar_numberErrorVisible.value = true
                isValid = false
            } else {
                balence_enquary_aadhar_numberError.value = ""
                balence_enquary_aadhar_numberErrorVisible.value = false
            }
        }


        if (balence_enquary_customer_number.value?.trim().isNullOrBlank()) {
            balence_enquary_customer_numberError.value = "This field is required"
            balence_enquary_customer_numberErrorVisible.value = true
            isValid = false
        } else {
            balence_enquary_customer_numberError.value = ""
            balence_enquary_customer_numberErrorVisible.value = false
        }
        return isValid
    }

    fun loanValidation(): Boolean {
        invisibleErrorTexts()

        var isValid = true
        if (selectBank.value?.trim().isNullOrBlank()) {
            selectBankError.value = "Please select bank"
            selectBankErrorVisible.value = true
            isValid = false
        } else {
            selectBankError.value = ""
            selectBankErrorVisible.value = false
        }

        if (loanAccountNumber.value?.trim().isNullOrBlank()) {
            loanAccountNumberError.value = "Please enter loan account number"
            loanAccountNumberErrorVisible.value = true
            isValid = false
        } else {
            loanAccountNumberError.value = ""
            loanAccountNumberErrorVisible.value = false
        }

        if (loanAmount.value?.trim().isNullOrBlank()) {
            loanAmountError.value = "Please enter a loan amount."
            loanAmountErrorVisible.value = true
            isValid = false
        } else {
            loanAmountError.value = ""
            loanAmountErrorVisible.value = false
        }



        return isValid
    }

    fun operatorValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (operatorSubId.value?.trim().isNullOrBlank()) {
            operatorSubIdError.value = "This field is required"
            operatorSubIdErrorVisible.value = true
            isValid = false
        } else {
            operatorSubIdError.value = ""
            operatorSubIdErrorVisible.value = false
        }

        if (selectOperator.value?.trim().isNullOrBlank()) {
            selectOperatorError.value = "Please select an operator"
            selectOperatorErrorVisible.value = true
            isValid = false
        } else {
            selectOperatorError.value = ""
            selectOperatorErrorVisible.value = false
        }

        if (operatordthAmt.value?.trim().isNullOrBlank()) {
            operatordthAmtError.value = "Please enter a valid amount."
            operatordthAmtErrorVisible.value = true
            isValid = false
        } else {
            operatordthAmtError.value = ""
            operatordthAmtErrorVisible.value = false
        }



        return isValid
    }

    fun educationValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (instituteName.value?.trim().isNullOrBlank()) {
            instituteNameError.value = "This field is required"
            instituteNameErrorVisible.value = true
            isValid = false
        } else {
            instituteNameError.value = ""
            instituteNameErrorVisible.value = false
        }

        if (selectInstitute.value?.trim().isNullOrBlank()) {
            selectInstituteError.value = "Please select institute type"
            selectInstituteErrorVisible.value = true
            isValid = false
        } else {
            selectInstituteError.value = ""
            selectInstituteErrorVisible.value = false
        }

        if (education_bank_name.value?.trim().isNullOrBlank()) {
            education_bank_nameError.value = "Select Bank"
            education_bank_nameErrorVisible.value = true
            isValid = false
        } else {
            education_bank_nameError.value = ""
            education_bank_nameErrorVisible.value = false
        }
        if (educationAmt.value?.trim().isNullOrBlank()) {
            educationAmtError.value = "Enter Amount"
            educationAmtErrorVisible.value = true
            isValid = false
        } else {
            educationAmtError.value = ""
            educationAmtErrorVisible.value = false
        }
        return isValid
    }

    fun bookACylinderValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (gasbookingNumber.value?.trim().isNullOrBlank()) {
            gasbookingNumberError.value = "This field is required"
            gasbookingNumberErrorVisible.value = true
            isValid = false
        } else {
            gasbookingNumberError.value = ""
            gasbookingNumberErrorVisible.value = false
        }

        if (gasBiller.value?.trim().isNullOrBlank()) {
            gasBillerError.value = "Please select institute type"
            gasBillerErrorVisible.value = true
            isValid = false
        } else {
            gasBillerError.value = ""
            gasBillerErrorVisible.value = false
        }


        if (gasBookingAmt.value?.trim().isNullOrBlank()) {
            gasBookingAmtError.value = "Enter Amount"
            gasBookingAmtErrorVisible.value = true
            isValid = false
        } else {
            gasBookingAmtError.value = ""
            gasBookingAmtErrorVisible.value = false
        }
        return isValid
    }


    fun mobileforRetailerValidation(): Boolean {

        invisibleErrorTexts()

        var isValid = true
        if (ratailerMobile.value?.trim().isNullOrBlank()) {
            ratailerMobileMobileError.value = "Mobile number is required"
            ratailerMobileMobileErrorVisible.value = true
            isValid = false
        } else {
            ratailerMobileMobileError.value = ""
            ratailerMobileMobileErrorVisible.value = false
        }
        return isValid
    }
     fun addRetailerValidation(): Boolean {



        var isValid = true
        if (ratailerName.value?.trim().isNullOrBlank()) {
            ratailerNameError.value = "This field is required"
            ratailerNameErrorVisible.value = true
            isValid = false
        } else {
            ratailerNameError.value = ""
            ratailerNameErrorVisible.value = false
        }
         if (ratailerShopName.value?.trim().isNullOrBlank()) {
             ratailerShopNameError.value = "This field is required"
             ratailerShopNameErrorVisible.value = true
            isValid = false
        } else {
             ratailerShopNameError.value = ""
             ratailerShopNameErrorVisible.value = false
        }
        if (ratailerEmail.value?.trim().isNullOrBlank()) {
            ratailerEmailError.value = "This field is required"
            ratailerEmailErrorVisible.value = true
            isValid = false
        } else {


            if (ratailerEmail.value?.trim()?.validate("email")==false){
                ratailerEmailError.value = "Email is not valid"
                ratailerEmailErrorVisible.value = true
                isValid = false
            } else {
                ratailerEmailError.value = ""
                ratailerEmailErrorVisible.value = false
            }



        }
         if (ratailerAddress.value?.trim().isNullOrBlank()) {
             ratailerAddressError.value = "This field is required"
             ratailerAddressErrorVisible.value = true
            isValid = false
        } else {
             ratailerAddressError.value = ""
             ratailerAddressErrorVisible.value = false
        }


        return isValid
    }

    fun invisibleErrorTexts() {
        mobileErrorVisible.value = false
        operatorErrorVisible.value = false
        amtErrorErrorVisible.value = false


        subIdErrorVisible.value = false
        dthErrorErrorVisible.value = false
        dthAmtErrorVisible.value = false

        consumerIdErrorVisible.value = false
        consumerIdPriceErrorVisible.value = false


        oldPinErrorVisible.value = false
        newPinErrorVisible.value = false
        confirmPinErrorVisible.value = false

        oldTPinErrorVisible.value = false
        newTPinErrorVisible.value = false
        confirmTPinErrorVisible.value = false


        credit_amt_ErrorVisible.value = false
        credit_mobileErrorVisible.value = false
        credit_remarks_ErrorVisible.value = false
        credit_holder_name_ErrorVisible.value = false
        credit_cardErrorVisible.value = false

    }


    val authLogin: LiveData<ResponseState<BaseResponse<LoginResponse>>>
        get() = repository.loginResponseLiveData


    val profileResponse: LiveData<ResponseState<profileResponse>>
        get() = repository.profileResponseLiveData

    fun profile(token: String, data: String) {
        viewModelScope.launch {
            repository.profile(token, data)
        }
    }

    val otpResponse: LiveData<ResponseState<OtpResponse>>
        get() = repository.otpResponseLiveData

    fun sendOtp(token: String, data: String) {
        viewModelScope.launch {
            repository.otp(token, data)
        }
    }


    val profile2Response: LiveData<ResponseState<profileResponse>>
        get() = repository.profile2ResponseLiveData

    fun profile2(token: String, data: String) {
        viewModelScope.launch {
            repository.profile2(token, data)
        }
    }

    //paymentReport
    val paymentReportResponseLiveData: LiveData<ResponseState<PaymentReportResponse>>
        get() = repository.paymentReportResponseLiveData

    fun paymentReport(token: String, data: String) {
        viewModelScope.launch {
            repository.paymentReport(token, data)
        }
    }

    //transcationReport
    val ranscationReportResponseLiveData: LiveData<ResponseState<TransactionReportResponse>>
        get() = repository._ranscationReportResponseLiveData

    fun transcationReport(token: String, data: String) {
        viewModelScope.launch {
            repository.transcationReport(token, data)
        }
    }

    //dmtReport
    val dmtReportResponseLiveData: LiveData<ResponseState<DmtReportReportModel>>
        get() = repository.dmtReportResponseLiveData

    fun dmtReport(token: String, data: String) {
        viewModelScope.launch {
            repository.dmtReport(token, data)
        }
    }

    //loadRequestReport
    val loadRequestReportResponseLiveData: LiveData<ResponseState<loadRequestModel>>
        get() = repository.loadRequestReportResponseLiveData

    fun loadRequestReport(token: String, data: String) {
        viewModelScope.launch {
            repository.loadRequestReport(token, data)
        }
    }

    //walletLedgerReport
    val walletLedgerReportResponseLiveData: LiveData<ResponseState<WalletLedgerModel>>
        get() = repository.walletLedgerReportResponseLiveData

    fun walletLedgerReport(token: String, data: String) {
        viewModelScope.launch {
            repository.walletLedgerReport(token, data)
        }
    }

    //aepsReport
    val aepsReportResponseLiveData: LiveData<ResponseState<aepsReportModel>>
        get() = repository.aepsReportResponseLiveData

    fun aepsReport(token: String, data: String) {
        viewModelScope.launch {
            repository.aepsReport(token, data)
        }
    }

    //microatmReport
    val microatmReportResponseLiveData: LiveData<ResponseState<MicroatmReportModel>>
        get() = repository.microatmReportResponseLiveData

    fun microatmReport(token: String, data: String) {
        viewModelScope.launch {
            repository.microatmReport(token, data)
        }
    }

    //commissionReport
    val commissionReportResponseLiveData: LiveData<ResponseState<commissionReportModel>>
        get() = repository.commissionReportResponseLiveData

    fun commissionReport(token: String, data: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                repository.commissionReport(token, data)
            }
        }
    }

    //complaints_report
    /*val complaints_reportResponseLiveData: LiveData<ResponseState<WalletSettleReportModel>>
        get() = repository.complaints_reportReportResponseLiveData
    fun complaints_report(token: String, data: String) {
        viewModelScope.launch {
            repository.complaints_report(token,data)
        }
    }*/


    //complants Report
    val complaints_reportReportResponseLiveData: LiveData<ResponseState<complaints_reportMode>>
        get() = repository.complaints_reportReportResponseLiveData

    fun complaints_report(token: String, data: String) {
        viewModelScope.launch {
            repository.complaints_report(token, data)
        }
    }

    //walletSettleReport
    val walletSettleReportResponseLiveData: LiveData<ResponseState<WalletSettleReportModel>>
        get() = repository.walletSettleReportResponseLiveData

    fun walletSettleReport(token: String, data: String) {
        viewModelScope.launch {
            repository.walletSettleReport(token, data)
        }
    }


    //bank_settle_report
    val bank_settle_reportResponseLiveData: LiveData<ResponseState<Bank_settle_reportModel>>
        get() = repository.bank_settle_reportResponseLiveData

    fun bank_settle_report(token: String, data: String) {
        viewModelScope.launch {
            repository.bank_settle_report(token, data)
        }
    }


    //cashout_ledger_report
    val cashout_ledger_reportResponseLiveData: LiveData<ResponseState<Cashout_ledger_reportModel>>
        get() = repository.cashout_ledger_reportResponseLiveData

    fun cashout_ledger_report(token: String, data: String) {
        viewModelScope.launch {
            repository.cashout_ledger_report(token, data)
        }
    }


    //postpaid mobile operator list
    val postpaid_mobile_operator_listResponseLiveData: LiveData<ResponseState<PostPaidMobileOperatorListModel>>
        get() = repository.postpaid_mobile_operator_listResponseLiveData

    fun postpaid_mobile_operator_list(token: String, data: String) {
        viewModelScope.launch {
            repository.postpaid_mobile_operator_list(token, data)
        }
    }

    //prepaid mobile operator list
    val prepaid_mobile_operator_listResponseLiveData: LiveData<ResponseState<PrePaidMobileOperatorListModel>>
        get() = repository.prepaid_mobile_operator_listResponseLiveData

    fun prepaid_mobile_operator_list(token: String, data: String) {
        viewModelScope.launch {
            repository.prepaid_mobile_operator_list(token, data)
        }
    }


    //prepaid mobile list
    val prePaidMobilePlainListResponseLiveData: LiveData<ResponseState<PrepaidMobolePlainModel>>
        get() = repository.prePaidMobilePlainListResponseLiveData

    fun prePaidMobilePlainList(token: String, data: String) {
        viewModelScope.launch {
            repository.prePaidMobilePlainList(token, data)
        }
    }


    //credit card send otp
    val creditCardSendOtpResponseLiveData: MutableLiveData<ResponseState<CreditCardSendOtpModel>>
        get() = repository.creditCardSendOtpResponseLiveData

    fun creditSendVerifyOtp(token: String, data: String) {
        viewModelScope.launch {
            repository.creditSendVerifyOtp(token, data)
        }
    }

    //credit card verify otp
    val creditCardVeryfyOTPResponseLiveData: MutableLiveData<ResponseState<CreditCardVerifyOtpModel>>
        get() = repository.creditCardVeryfyOTPResponseLiveData

    fun creditCardVeryfyOTP(token: String, data: String) {
        viewModelScope.launch {
            repository.creditCardVeryfyOTP(token, data)
        }
    }

    //Epotly transpher
    val epotlyTranspherResponseLiveData: MutableLiveData<ResponseState<EPotlyTranspherModel>>
        get() = repository.epotlyTranspherResponseLiveData

    fun epotlyTranspher(token: String, data: String) {
        viewModelScope.launch {
            repository.epotlyTranspher(token, data)
        }
    }

    val changePinResponseLiveData: MutableLiveData<ResponseState<ChangeUserPasswordModel>>
        get() = repository.changePinResponseLiveData

    fun changePin(token: String, data: String) {
        viewModelScope.launch {
            repository.changePin(token, data)
        }
    }

    val changeTPinResponseLiveData: MutableLiveData<ResponseState<ChangeUserTPINPasswordModel>>
        get() = repository.changeTPinResponseLiveData

    fun changeTPin(token: String, data: String) {
        viewModelScope.launch {
            repository.changeTPin(token, data)
        }
    }


    //credit card send otp
    val transcationReportReceiptResponseLiveData: MutableLiveData<ResponseState<TransactionReportModel>>
        get() = repository.transcationReportReceiptResponseLiveData

    fun transcationReportReceipt(token: String, data: String) {
        viewModelScope.launch {
            repository.transcationReportReceipt(token, data)
        }
    }


    //dmt ReportReceipt
    val dmtReportReceiptResponseLiveData: MutableLiveData<ResponseState<DMTReportModel>>
        get() = repository.dmtReportReceiptResponseLiveData

    fun dmtReportReceipt(token: String, data: String) {
        viewModelScope.launch {
            repository.dmtReportReceipt(token, data)
        }
    }

    //aeps ReportReceipt
    val aepsReportReceiptReceiptResponseLiveData: MutableLiveData<ResponseState<AEPSReportModel>>
        get() = repository.aepsReportReceiptReceiptResponseLiveData

    fun aepsReportReceiptReceipt(token: String, data: String) {
        viewModelScope.launch {
            repository.aepsReportReceiptReceipt(token, data)
        }
    }
    /*

        //Check service
        val checkServiceReceiptResponseLiveData: MutableLiveData<ResponseState<CheckServiceModel>>
            get() = repository.checkServiceResponseLiveData
        fun checkService(token: String, data: String) {
            viewModelScope.launch {
                repository.checkService(token,data)
            }
        }

        //Check service
        val checkServiceReceiptHomePageResponseLiveData: MutableLiveData<ResponseState<CheckServiceModel>>
            get() = repository.checkServiceHomeResponseLiveData
        fun checkHomePageService(token: String, data: String) {
            viewModelScope.launch {
                repository.checkServiceHome(token,data)
            }
        }

    */


    //matm reports
    val microatm_report_receiptResponseReceptLiveData: MutableLiveData<ResponseState<MatmeportModel>>
        get() = repository.microatm_report_receiptResponseReceptLiveData

    fun microatmReportReceipt(token: String, data: String) {
        viewModelScope.launch {
            repository.microatmReportReceipt(token, data)
        }
    }

    //reset TPIN
    val resetTPINResponseReceptLiveData: MutableLiveData<ResponseState<ResetTPINModel>>
        get() = repository.resetTPINResponseReceptLiveData

    fun resetTPINResponse(token: String, data: String) {
        viewModelScope.launch {
            repository.resetTPIN(token, data)
        }
    }

    //pattern Login
    val patternLoginReceptLiveData: MutableLiveData<ResponseState<PatternLoginModel>>
        get() = repository.patternLoginModelReceptLiveData

    fun patternLogin(token: String, data: String) {
        viewModelScope.launch {
            repository.patternLogin(token, data)
        }
    }


    //move to bank
    val moveToBankReceptLiveData: MutableLiveData<ResponseState<MoveToBankBankListModel>>
        get() = repository.moveToBankReceptLiveData

    fun moveToBank(token: String, data: String) {
        viewModelScope.launch {
            repository.moveToBank(token, data)
        }
    }

    //submit move to bank
    val submit_moveToBankReceptLiveData: MutableLiveData<ResponseState<SubmitMoveToBankBankListModel>>
        get() = repository.submit_moveToBankReceptLiveData

    fun submitMovetobank(token: String, data: String) {
        viewModelScope.launch {
            repository.submitMovetobank(token, data)
        }
    }

    //add Bank
    val addToBankReceptLiveData: MutableLiveData<ResponseState<AddBankModel>>
        get() = repository.addToBankReceptLiveData

    fun addToBank(token: String, data: String, imagedata: MultipartBody.Part?) {
        viewModelScope.launch {
            repository.addToBank(token, data, imagedata)
        }
    }

    // Move To wallet
    val moveToWalletLiveData: MutableLiveData<ResponseState<MoveToWalletModel>>
        get() = repository.moveToWalletLiveData

    fun moveToWallet(token: String, data: String) {
        viewModelScope.launch {
            repository.moveToWallet(token, data)
        }
    }

    // $Move To wallet$
    val submitMoveToWalletLiveData: MutableLiveData<ResponseState<SubmitMoveToBankBankListModel>>
        get() = repository.submitMoveToWalletLiveData

    fun submitMoveToWallet(token: String, data: String) {
        viewModelScope.launch {
            repository.submitMoveToWallet(token, data)
        }
    }

    private val _onboardingBasicinfoResponseLiveData =
        MutableLiveData<ResponseState<BasicInfo>>()
    val onboardingBasicinfoResponseLiveData: LiveData<ResponseState<BasicInfo>>
        get() = _onboardingBasicinfoResponseLiveData

    //bankList
    val bankListResponseLiveData: LiveData<ResponseState<AllBankListModel>>
        get() = repository.bankListResponseLiveData

    fun bankList(token: String, data: String) {
        viewModelScope.launch {
            repository.bankList(token, data)
        }
    }

    //PaymentREquistMode
    val PaymentREquistModeResponseLiveData: LiveData<ResponseState<PaymentREquistModeModel>>
        get() = repository.PaymentREquistModeResponseLiveData

    fun PaymentREquistMode(token: String, data: String) {
        viewModelScope.launch {
            repository.PaymentREquistMode(token, data)
        }
    }

    //PaymentRequist
    val PaymentRequistResponseLiveData: LiveData<ResponseState<PaymentRequistModel>>
        get() = repository.PaymentRequistResponseLiveData

    fun PaymentRequist(
        token: String,
        data: String,
        paymentSlip: MultipartBody.Part?,
        denomSlip: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            repository.PaymentRequist(token, data, paymentSlip, denomSlip)
        }
    }

    //ServiceCheck
    val ServiceCheckResponseLiveData: MutableLiveData<ResponseState<ServiceCheckModel>>
        get() = repository.ServiceCheckResponseLiveData

    fun ServiceCheck(token: String, data: String) {
        viewModelScope.launch {
            repository.ServiceCheck(token, data)
        }
    }

    //ServiceCheckViewMore
    val ServiceCheckViewMoreResponseLiveData: MutableLiveData<ResponseState<ServiceCheckModel>>
        get() = repository.ServiceCheckViewMoreResponseLiveData

    fun ServiceCheckViewMore(token: String, data: String) {
        viewModelScope.launch {
            repository.ServiceCheckViewMore(token, data)
        }
    }

    //addBankBankList
    val addBankBankListResponseLiveData: LiveData<ResponseState<AddBankBankListModel>>
        get() = repository.addBankBankListResponseLiveData

    fun addBankBankList(token: String, data: String) {
        viewModelScope.launch {
            repository.addBankBankList(token, data)
        }
    }


    //addBankBankList
    val forgotPasswordResponseLiveData: MutableLiveData<ResponseState<ForgotPasswordModel>>
        get() = repository.forgotPasswordResponseLiveData

    fun forgotPassword(token: String, data: String) {
        viewModelScope.launch {
            repository.forgotPassword(token, data)
        }
    }


    //addBankBankList
    val forgotPasswordVerifyOtpResponseLiveData: MutableLiveData<ResponseState<ForgotPasswordVerifyOtpModel>>
        get() = repository.forgotPasswordVerifyOtpResponseLiveData

    fun ForgotPasswordVerifyOtp(token: String, data: String) {
        viewModelScope.launch {
            repository.ForgotPasswordVerifyOtp(token, data)
        }
    }


    //refreshToken
    val refreshTokenResponseLiveData: LiveData<ResponseState<refreshTokenModel>>
        get() = repository.refreshTokenResponseLiveData

    fun refreshToken(token: String, data: String) {
        viewModelScope.launch {
            repository.refreshToken(token, data)
        }
    }

    //app update
    val appupdateResponseLiveData: LiveData<ResponseState<appUpdateUrlModel>>
        get() = repository.appUpdateResponseLiveData

    fun appUpdate(token: String, data: String) {
        viewModelScope.launch {
            repository.appUpdate(token, data)
        }
    }


    //cashCollection
    val cashCollectionResponseLiveData: MutableLiveData<ResponseState<cashCollectionModel>>
        get() = repository.cashCollectionResponseLiveData

    fun cashCollection(token: String, data: String, insuranceOrCashCollection: String) {
        viewModelScope.launch {
            repository.cashCollection(token, data, insuranceOrCashCollection)
        }
    }


    //insurance
    val insuranceResponseLiveData: MutableLiveData<ResponseState<InsuranceModel>>
        get() = repository.insuranceResponseLiveData

    fun insurance(token: String, data: String, insuranceOrCashCollection: String) {
        viewModelScope.launch {
            repository.insurance(token, data, insuranceOrCashCollection)
        }
    }


    //checkUser
    val checkUserResponseLiveData: MutableLiveData<ResponseState<checkUserModel>>
        get() = repository.checkUserResponseLiveData

    fun checkUser(token: String, data: String) {
        viewModelScope.launch {
            repository.checkUser(token, data)
        }
    }


    val checkUserResponseLiveData2: MutableLiveData<ResponseState<checkUserModel>>
        get() = repository.checkUserResponseLiveData2

    fun checkUser2(token: String, data: String) {
        viewModelScope.launch {
            repository.checkUser2(token, data)
        }
    }





    //beneficiaryList
    val beneficiaryListResponseLiveData: MutableLiveData<ResponseState<beneficiaryListModel>>
        get() = repository.beneficiaryListResponseLiveData

    fun beneficiaryList(token: String, data: String) {
        viewModelScope.launch {
            repository.beneficiaryList(token, data)
        }
    }





    //addRemitter
    val addRemitterResponseLiveData: MutableLiveData<ResponseState<addRemitterModel>>
        get() = repository.addRemitterResponseLiveData

    fun addRemitter(token: String, data: String) {
        viewModelScope.launch {
            repository.addRemitter(token, data)
        }
    }


    //addBeneficiary
    val addBeneficiaryResponseLiveData: MutableLiveData<ResponseState<addBeneficiaryModel>>
        get() = repository.addBeneficiaryResponseLiveData

    fun addBeneficiary(token: String, data: String) {
        viewModelScope.launch {
            repository.addBeneficiary(token, data)
        }
    }

    //beneficiary Verify
    val beneficiaryVerifyResponseLiveData: MutableLiveData<ResponseState<verifyBeneficiaryModel>>
        get() = repository.beneficiaryVerifyResponseLiveData

    fun beneficiaryVerify(token: String, data: String) {
        viewModelScope.launch {
            repository.beneficiaryVerify(token, data)
        }
    }

    //moneyTransfer
    val moneyTransferResponseLiveData: MutableLiveData<ResponseState<MoneyTranspherModel>>
        get() = repository.moneyTransferResponseLiveData

    fun moneyTransfer(token: String, data: String) {
        viewModelScope.launch {
            repository.moneyTransfer(token, data)
        }
    }


    //electricStatelist
    val electricStatelistResponseLiveData: MutableLiveData<ResponseState<electricStatelistModel>>
        get() = repository.electricStatelistResponseLiveData

    fun electricStatelist(token: String, data: String) {
        viewModelScope.launch {
            repository.electricStatelist(token, data)
        }
    }


    //electricbillerlist
    val electricbillerlistResponseLiveData: MutableLiveData<ResponseState<billerlistModel>>
        get() = repository.electricbillerlistResponseLiveData

    fun electricbillerlist(token: String, data: String) {
        viewModelScope.launch {
            repository.electricbillerlist(token, data)
        }
    }


    //electricBillbillFetch
    val electricBillbillFetchResponseLiveData: MutableLiveData<ResponseState<electricBillbillFetchModel>>
        get() = repository.electricBillbillFetchResponseLiveData

    fun electricBillbillFetch(token: String, data: String) {
        viewModelScope.launch {
            repository.electricBillbillFetch(token, data)
        }
    }


    //billpaytransaction
    val billpaytransactionResponseLiveData: MutableLiveData<ResponseState<billpaytransactionModel>>
        get() = repository.billpaytransactionResponseLiveData

    fun billpaytransaction(token: String, data: String) {
        viewModelScope.launch {
            repository.billpaytransaction(token, data)
        }
    }


    //add retailer
    val addRetailerResponseLiveData: MutableLiveData<ResponseState<AddretailerModel>>
        get() = repository.addRetailerResponseLiveData

    fun addRetailer(token: String, data: String) {
        viewModelScope.launch {
            repository.addRetailer(token, data)
        }
    }


    //add retailer
    val viewRetailerModelResponseLiveData: MutableLiveData<ResponseState<ViewRetailerModel>>
        get() = repository.viewRetailerModelResponseLiveData

    fun ViewRetailerModel(token: String, data: String) {
        viewModelScope.launch {
            repository.ViewRetailerModel(token, data)
        }
    }

    //add retailer details
    val add_retailer_dtlsResponseLiveData: MutableLiveData<ResponseState<AddRetailarDetailsModel>>
        get() = repository.add_retailer_dtlsResponseLiveData

    fun add_retailer_dtls(token: String, data: String) {
        viewModelScope.launch {
            repository.add_retailer_dtls(token, data)
        }
    }
   //PayPartnerModel
    val pay_partnerResponseLiveData: MutableLiveData<ResponseState<PayPartnerModel>>
        get() = repository.pay_partnerResponseLiveData

    fun pay_partner(token: String, data: String) {
        viewModelScope.launch {
            repository.pay_partner(token, data)
        }
    }





    val StateListResponseLiveData: LiveData<ResponseState<StateListModel>>
        get() = repository.StateListResponseLiveData
    fun StateList(token: String, data: String) {
        viewModelScope.launch {
            repository.StateList(token,data)
        }
    }

    //CityList
    val CityListResponseLiveData: LiveData<ResponseState<CityListModel>>
        get() = repository.CityListResponseLiveData
    fun CityList(token: String, data: String) {
        viewModelScope.launch {
            repository.CityList(token,data)
        }
    }

    //Check merchant
    val checkmerchantResponseLiveData: MutableLiveData<ResponseState<CheckMerchant>>
        get() = repository.checkmerchantResponseLiveData
    fun checkmerchant(token: String, data: String) {
        viewModelScope.launch {
            repository.checkmerchant(token,data)
        }
    }



}
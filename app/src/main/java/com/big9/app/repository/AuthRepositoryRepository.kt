package com.big9.app.repository

import AddRetailarDetailsModel
import AddretailerModel
import InsuranceModel
import MoneyTranspherModel
import PayPartnerModel
import ViewRetailerModel
import addBeneficiaryModel
import addRemitterModel
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.big9.app.data.model.DTHTranspherModel
import com.big9.app.data.model.DTHUserInfoModel
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
import com.big9.app.data.model.PrepaidMoboleTranspherModel
import com.big9.app.data.model.ResetTPINModel
import com.big9.app.data.model.ServiceCheckModel
import com.big9.app.data.model.SubmitMoveToBankBankListModel
import com.big9.app.data.model.TransactionReportModel
import com.big9.app.data.model.allReport.Bank_settle_reportModel
import com.big9.app.data.model.allReport.Cashout_ledger_reportModel
import com.big9.app.data.model.allReport.DmtReportReportModel
import com.big9.app.data.model.allReport.MicroatmReportModel
import com.big9.app.data.model.allReport.PostPaidMobileOperatorListModel
import com.big9.app.data.model.allReport.PostPaidMobileTranspherModel
import com.big9.app.data.model.allReport.TransactionReportResponse
import com.big9.app.data.model.allReport.receipt.Transcation_report_receiptReportModel
import com.big9.app.data.model.allReport.WalletLedgerModel
import com.big9.app.data.model.allReport.WalletSettleReportModel
import com.big9.app.data.model.allReport.aepsReportModel
import com.big9.app.data.model.allReport.commissionReportModel
import com.big9.app.data.model.allReport.complaints_reportMode
import com.big9.app.data.model.allReport.loadRequestModel
import com.big9.app.data.model.allReport.receipt.Microatm_report_receipt
import com.big9.app.data.model.bankDetailsModel
import com.big9.app.data.model.banknameModel
import com.big9.app.data.model.businessCategoryModel
import com.big9.app.data.model.businesstypeMethod
import com.big9.app.data.model.companyDetailsModel
import com.big9.app.data.model.login.LoginResponse
import com.big9.app.data.model.onBoading.DocumentUploadModel
import com.big9.app.data.model.onBoading.RegForm
import com.big9.app.data.model.onBoardindPackage.BasicInfo
import com.big9.app.data.model.onBoardindPackage.CityListModel
import com.big9.app.data.model.onBoardindPackage.StateListModel
import com.big9.app.data.model.otp.OtpResponse
import com.big9.app.data.model.paymentReport.PaymentReportResponse
import com.big9.app.data.model.profile.profileResponse
import com.big9.app.data.model.sample.Test
import com.big9.app.network.ResponseState
import com.big9.app.network.RetroApi
import electricBillbillFetchModel
import electricStatelistModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import refreshTokenModel
import verifyBeneficiaryModel


import javax.inject.Inject

class AuthRepositoryRepository @Inject constructor(private val api: RetroApi) {
    private val _formResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<Test>>>()
    val formResponseLiveData: LiveData<ResponseState<BaseResponse<Test>>>
        get() = _formResponseLiveData


    suspend fun formReg(requestBody: RegForm) {
        _formResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.formReg(requestBody)
            _formResponseLiveData.postValue(ResponseState.create(response, "aa"))
            Log.e("TAG_error", "login:OK " + response)

        } catch (throwable: Throwable) {
            _formResponseLiveData.postValue(ResponseState.create(throwable))
            Log.e("TAG_error", "login: " + throwable.message)
        }

    }


    //Doc upload

    private val _docUploadResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<Test>>>()
    val docUploadResponseLiveData: LiveData<ResponseState<BaseResponse<Test>>>
        get() = _docUploadResponseLiveData


    suspend fun docUpload(requestBody: DocumentUploadModel) {
        _docUploadResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.docUpload(requestBody)
            _docUploadResponseLiveData.postValue(ResponseState.create(response, "aa"))
            //Log.e("TAG_error", "login:OK "+response)

        } catch (throwable: Throwable) {
            _docUploadResponseLiveData.postValue(ResponseState.create(throwable))
            //Log.e("TAG_error", "login: "+throwable.message)
        }

    }


    //Login Model

    private val _loginResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<LoginResponse>>>()
    val loginResponseLiveData: MutableLiveData<ResponseState<BaseResponse<LoginResponse>>>
        get() = _loginResponseLiveData


    suspend fun userLogin(loginModel: String) {
        _loginResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response =
                api.epayLogin(loginModel.replace("\n", "").replace("\r", ""), "loginModel")
            _loginResponseLiveData.postValue(ResponseState.create(response, "aa"))


        } catch (throwable: Throwable) {
            _loginResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //OTP

    private val _otpResponseLiveData =
        MutableLiveData<ResponseState<OtpResponse>>()
    val otpResponseLiveData: LiveData<ResponseState<OtpResponse>>
        get() = _otpResponseLiveData


    suspend fun otp(token: String, loginModel: String) {
        _otpResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.otpverify(token, loginModel.replace("\n", "").replace("\r", ""))
            _otpResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _otpResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //Profile2
    private val _profile2ResponseLiveData =
        MutableLiveData<ResponseState<profileResponse>>()
    val profile2ResponseLiveData: LiveData<ResponseState<profileResponse>>
        get() = _profile2ResponseLiveData


    suspend fun profile2(token: String, loginModel: String) {
        _profile2ResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.profile2(token, loginModel.replace("\n", "").replace("\r", ""))
            _profile2ResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _profile2ResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //profile

    private val _profileResponseLiveData =
        MutableLiveData<ResponseState<profileResponse>>()
    val profileResponseLiveData: LiveData<ResponseState<profileResponse>>
        get() = _profileResponseLiveData


    suspend fun profile(token: String, loginModel: String) {
        _profileResponseLiveData.postValue(ResponseState.Loading())
        try {
            var token =
                "eyJ1c2VyX2lkIjoiIiwidGltZXN0YW1wIjoxNzAxOTM5MTk2LCJyYW5kb20iOiJhN2ZjZWRjMjM1NzkxOGJlZDdjNjY2OGJjYmVhYmNhMmU4NWNhYWIwODE2ODg5ZjdhODY5YTY3MzBmNWY3Y2MzIn0="
            var data = "ZqHbVba0bT2zVD2pxkGEH9tsoSvGvl18BD8ZpvkXvtM="
            val response = api.profile(token, data.replace("\n", "").replace("\r", ""))
            _profileResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _profileResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //paymentReport
    private val _paymentReportResponseLiveData =
        MutableLiveData<ResponseState<PaymentReportResponse>>()
    val paymentReportResponseLiveData: LiveData<ResponseState<PaymentReportResponse>>
        get() = _paymentReportResponseLiveData


    suspend fun paymentReport(token: String, data: String) {
        _paymentReportResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.paymentReport(token, data.replace("\n", "").replace("\r", ""))
            _paymentReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _paymentReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //transcationReport
    private val _transcationReportResponseLiveData =
        MutableLiveData<ResponseState<TransactionReportResponse>>()
    val _ranscationReportResponseLiveData: LiveData<ResponseState<TransactionReportResponse>>
        get() = _transcationReportResponseLiveData


    suspend fun transcationReport(token: String, loginModel: String) {
        _transcationReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.transcationReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _transcationReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _transcationReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //dmtReport
    private val _dmtReportResponseLiveData =
        MutableLiveData<ResponseState<DmtReportReportModel>>()
    val dmtReportResponseLiveData: LiveData<ResponseState<DmtReportReportModel>>
        get() = _dmtReportResponseLiveData


    suspend fun dmtReport(token: String, loginModel: String) {
        _dmtReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.dmtReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _dmtReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _dmtReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //loadRequestReport
    private val _loadRequestReportResponseLiveData =
        MutableLiveData<ResponseState<loadRequestModel>>()
    val loadRequestReportResponseLiveData: LiveData<ResponseState<loadRequestModel>>
        get() = _loadRequestReportResponseLiveData


    suspend fun loadRequestReport(token: String, loginModel: String) {
        _loadRequestReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.loadRequestReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _loadRequestReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _loadRequestReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //walletLedgerReport
    private val _walletLedgerReportResponseLiveData =
        MutableLiveData<ResponseState<WalletLedgerModel>>()
    val walletLedgerReportResponseLiveData: LiveData<ResponseState<WalletLedgerModel>>
        get() = _walletLedgerReportResponseLiveData


    suspend fun walletLedgerReport(token: String, loginModel: String) {
        _walletLedgerReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.walletLedgerReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _walletLedgerReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _walletLedgerReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //aepsReport
    private val _aepsReportResponseLiveData =
        MutableLiveData<ResponseState<aepsReportModel>>()
    val aepsReportResponseLiveData: LiveData<ResponseState<aepsReportModel>>
        get() = _aepsReportResponseLiveData


    suspend fun aepsReport(token: String, loginModel: String) {
        _aepsReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.aepsReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _aepsReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _aepsReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //microatmReport
    private val _microatmReportResponseLiveData =
        MutableLiveData<ResponseState<MicroatmReportModel>>()
    val microatmReportResponseLiveData: LiveData<ResponseState<MicroatmReportModel>>
        get() = _microatmReportResponseLiveData


    suspend fun microatmReport(token: String, loginModel: String) {
        _microatmReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.microatmReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _microatmReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _microatmReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //commissionReport
    private val _commissionReportResponseLiveData =
        MutableLiveData<ResponseState<commissionReportModel>>()
    val commissionReportResponseLiveData: LiveData<ResponseState<commissionReportModel>>
        get() = _commissionReportResponseLiveData


    suspend fun commissionReport(token: String, loginModel: String) {
        _commissionReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.commissionReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _commissionReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _commissionReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    /*//complaints_report
    private val _complaints_reportResponseLiveData =
        MutableLiveData<ResponseState<WalletSettleReportModel>>()
    val complaints_reportResponseLiveData: LiveData<ResponseState<WalletSettleReportModel>>
        get() = _complaints_reportResponseLiveData


    suspend fun complaints_report(token: String, loginModel: String) {
        _complaints_reportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.walletSettleReportTest(token, loginModel.replace("\n", "").replace("\r", ""))
            _complaints_reportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _complaints_reportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }*/


    //Complaints
    private val _complaints_reportReportResponseLiveData =
        MutableLiveData<ResponseState<complaints_reportMode>>()
    val complaints_reportReportResponseLiveData: LiveData<ResponseState<complaints_reportMode>>
        get() = _complaints_reportReportResponseLiveData


    suspend fun complaints_report(token: String, loginModel: String) {
        _complaints_reportReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.complaints_report(token, loginModel.replace("\n", "").replace("\r", ""))
            _complaints_reportReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _complaints_reportReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //walletSettleReport
    private val _walletSettleReportResponseLiveData =
        MutableLiveData<ResponseState<WalletSettleReportModel>>()
    val walletSettleReportResponseLiveData: LiveData<ResponseState<WalletSettleReportModel>>
        get() = _walletSettleReportResponseLiveData


    suspend fun walletSettleReport(token: String, loginModel: String) {
        _walletSettleReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.walletSettleReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _walletSettleReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _walletSettleReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //bank_settle_report
    private val _bank_settle_reportResponseLiveData =
        MutableLiveData<ResponseState<Bank_settle_reportModel>>()
    val bank_settle_reportResponseLiveData: LiveData<ResponseState<Bank_settle_reportModel>>
        get() = _bank_settle_reportResponseLiveData


    suspend fun bank_settle_report(token: String, loginModel: String) {
        _bank_settle_reportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.bank_settle_report(token, loginModel.replace("\n", "").replace("\r", ""))
            _bank_settle_reportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _bank_settle_reportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //cashout_ledger_report
    private val _cashout_ledger_reportResponseLiveData =
        MutableLiveData<ResponseState<Cashout_ledger_reportModel>>()
    val cashout_ledger_reportResponseLiveData: LiveData<ResponseState<Cashout_ledger_reportModel>>
        get() = _cashout_ledger_reportResponseLiveData


    suspend fun cashout_ledger_report(token: String, loginModel: String) {
        _cashout_ledger_reportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.cashout_ledger_report(token, loginModel.replace("\n", "").replace("\r", ""))
            _cashout_ledger_reportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _cashout_ledger_reportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //transcation_report_receipt
    private val _transcation_report_receiptResponseLiveData =
        MutableLiveData<ResponseState<Transcation_report_receiptReportModel>>()
    val transcation_report_receiptResponseLiveData: LiveData<ResponseState<Transcation_report_receiptReportModel>>
        get() = _transcation_report_receiptResponseLiveData


    suspend fun transcation_report_receipt(token: String, loginModel: String) {
        _transcation_report_receiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.transcation_report_receipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _transcation_report_receiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _transcation_report_receiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Aeps_report_receipt
    private val _aeps_report_receiptResponseLiveData =
        MutableLiveData<ResponseState<Transcation_report_receiptReportModel>>()
    val aeps_report_receiptResponseLiveData: LiveData<ResponseState<Transcation_report_receiptReportModel>>
        get() = _aeps_report_receiptResponseLiveData


    suspend fun aeps_report_receiptResponseLiveData(token: String, loginModel: String) {
        _aeps_report_receiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.transcation_report_receipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _aeps_report_receiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _aeps_report_receiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Dmt_report_receiptModel
    private val _dmt_report_receiptModelResponseLiveData =
        MutableLiveData<ResponseState<Transcation_report_receiptReportModel>>()
    val dmt_report_receiptModelResponseLiveData: LiveData<ResponseState<Transcation_report_receiptReportModel>>
        get() = _dmt_report_receiptModelResponseLiveData


    suspend fun dmt_report_receiptModelResponseLiveData(token: String, loginModel: String) {
        _dmt_report_receiptModelResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.transcation_report_receipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _dmt_report_receiptModelResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _dmt_report_receiptModelResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Microatm_report_receipt
    private val _microatm_report_receiptResponseLiveData =
        MutableLiveData<ResponseState<Microatm_report_receipt>>()
    val microatm_report_receiptResponseLiveData: LiveData<ResponseState<Microatm_report_receipt>>
        get() = _microatm_report_receiptResponseLiveData


    suspend fun microatm_report_receiptResponseLiveData(token: String, loginModel: String) {
        _microatm_report_receiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.microatm_report_receipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _microatm_report_receiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _microatm_report_receiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Postpaid mobile operator list
    private val _postpaid_mobile_operator_listResponseLiveData =
        MutableLiveData<ResponseState<PostPaidMobileOperatorListModel>>()
    val postpaid_mobile_operator_listResponseLiveData: LiveData<ResponseState<PostPaidMobileOperatorListModel>>
        get() = _postpaid_mobile_operator_listResponseLiveData


    suspend fun postpaid_mobile_operator_list(token: String, loginModel: String) {
        _postpaid_mobile_operator_listResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.MobilePostPaidOperatorList(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _postpaid_mobile_operator_listResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _postpaid_mobile_operator_listResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Prepaid mobile operator list
    private val _prepaid_mobile_operator_listResponseLiveData =
        MutableLiveData<ResponseState<PrePaidMobileOperatorListModel>>()
    val prepaid_mobile_operator_listResponseLiveData: LiveData<ResponseState<PrePaidMobileOperatorListModel>>
        get() = _prepaid_mobile_operator_listResponseLiveData


    suspend fun prepaid_mobile_operator_list(token: String, loginModel: String) {
        _prepaid_mobile_operator_listResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.MobilePrePaidOperatorList(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _prepaid_mobile_operator_listResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            Log.d("TAGjsondata", "prepaid_mobile_operator_list: " + throwable.message)
            _prepaid_mobile_operator_listResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //Postpaid mobile transpher
    private val _postPaidMobileTranspherResponseLiveData =
        MutableLiveData<ResponseState<PostPaidMobileTranspherModel>>()
    val postPaidMobileTranspherResponseLiveData: MutableLiveData<ResponseState<PostPaidMobileTranspherModel>>
        get() = _postPaidMobileTranspherResponseLiveData


    suspend fun PostPaidMobileTranspher(token: String, loginModel: String) {
        _postPaidMobileTranspherResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.PostPaidMobileTranspher(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _postPaidMobileTranspherResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _postPaidMobileTranspherResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //Prepaid mobile transpher
    private val _prePaidMobilePlainListResponseLiveData =
        MutableLiveData<ResponseState<PrepaidMobolePlainModel>>()
    val prePaidMobilePlainListResponseLiveData: LiveData<ResponseState<PrepaidMobolePlainModel>>
        get() = _prePaidMobilePlainListResponseLiveData


    suspend fun prePaidMobilePlainList(token: String, loginModel: String) {
        _prePaidMobilePlainListResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.MobilePrePaidPlainList(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _prePaidMobilePlainListResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _prePaidMobilePlainListResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //Prepaid mobile transpher
    private val _prePaidMobileTranspherResponseLiveData =
        MutableLiveData<ResponseState<PrepaidMoboleTranspherModel>>()
    val prePaidMobileTranspherResponseLiveData: MutableLiveData<ResponseState<PrepaidMoboleTranspherModel>>
        get() = _prePaidMobileTranspherResponseLiveData

    suspend fun PrePaidMobileTranspher(token: String, loginModel: String) {
        _prePaidMobileTranspherResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.MobilePrePaidpreTransfer(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _prePaidMobileTranspherResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            Log.d("TAG_mob", "PrePaidMobileTranspher: "+throwable.message)
            _prePaidMobileTranspherResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Credit card send otp
    private val _creditCardSendOtpResponseLiveData =
        MutableLiveData<ResponseState<CreditCardSendOtpModel>>()
    val creditCardSendOtpResponseLiveData: MutableLiveData<ResponseState<CreditCardSendOtpModel>>
        get() = _creditCardSendOtpResponseLiveData

    suspend fun creditSendVerifyOtp(token: String, loginModel: String) {
        _creditCardSendOtpResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.creditCardSendOtp(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _creditCardSendOtpResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _creditCardSendOtpResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Credit card verify otp
    private val _creditCardVeryfyOTPResponseLiveData =
        MutableLiveData<ResponseState<CreditCardVerifyOtpModel>>()
    val creditCardVeryfyOTPResponseLiveData: MutableLiveData<ResponseState<CreditCardVerifyOtpModel>>
        get() = _creditCardVeryfyOTPResponseLiveData

    suspend fun creditCardVeryfyOTP(token: String, loginModel: String) {
        _creditCardVeryfyOTPResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.creditCardverifyOtp(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _creditCardVeryfyOTPResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _creditCardVeryfyOTPResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //Epotly Transpher
    private val _epotlyTranspherResponseLiveData =
        MutableLiveData<ResponseState<EPotlyTranspherModel>>()
    val epotlyTranspherResponseLiveData: MutableLiveData<ResponseState<EPotlyTranspherModel>>
        get() = _epotlyTranspherResponseLiveData

    suspend fun epotlyTranspher(token: String, loginModel: String) {
        _epotlyTranspherResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.epotlyTransfer(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _epotlyTranspherResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _epotlyTranspherResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //DTH Operator List
    private val _dthTransferResponseLiveData =
        MutableLiveData<ResponseState<DTHTranspherModel>>()
    val dthTransferResponseLiveData: MutableLiveData<ResponseState<DTHTranspherModel>>
        get() = _dthTransferResponseLiveData

    suspend fun dthTransfer(token: String, loginModel: String) {
        _dthTransferResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.dthTransfer(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _dthTransferResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _dthTransferResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //DTH USER INFO
    private val _dthUserInfoResponseLiveData =
        MutableLiveData<ResponseState<DTHUserInfoModel>>()
    val dthUserInfoResponseLiveData: LiveData<ResponseState<DTHUserInfoModel>>
        get() = _dthUserInfoResponseLiveData

    suspend fun dthUserInfo(token: String, loginModel: String) {
        _dthUserInfoResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.dthUserInfo(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _dthUserInfoResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _dthUserInfoResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Change Passdord
    private val _changePinResponseLiveData =
        MutableLiveData<ResponseState<ChangeUserPasswordModel>>()
    val changePinResponseLiveData: MutableLiveData<ResponseState<ChangeUserPasswordModel>>
        get() = _changePinResponseLiveData

    suspend fun changePin(token: String, loginModel: String) {
        _changePinResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.changePin(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _changePinResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _changePinResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Change changeTpin
    private val _changeTPinResponseLiveData =
        MutableLiveData<ResponseState<ChangeUserTPINPasswordModel>>()
    val changeTPinResponseLiveData: MutableLiveData<ResponseState<ChangeUserTPINPasswordModel>>
        get() = _changeTPinResponseLiveData

    suspend fun changeTPin(token: String, loginModel: String) {
        _changeTPinResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.changeTpin(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _changeTPinResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _changeTPinResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //transcation Report Receipt
    private val _transcationReportReceiptResponseLiveData =
        MutableLiveData<ResponseState<TransactionReportModel>>()
    val transcationReportReceiptResponseLiveData: MutableLiveData<ResponseState<TransactionReportModel>>
        get() = _transcationReportReceiptResponseLiveData

    suspend fun transcationReportReceipt(token: String, loginModel: String) {
        _transcationReportReceiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.transcationReportReceipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _transcationReportReceiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _transcationReportReceiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //DMT Report Receipt
    private val _dmtReportReceiptResponseLiveData =
        MutableLiveData<ResponseState<DMTReportModel>>()
    val dmtReportReceiptResponseLiveData: MutableLiveData<ResponseState<DMTReportModel>>
        get() = _dmtReportReceiptResponseLiveData

    suspend fun dmtReportReceipt(token: String, loginModel: String) {
        _dmtReportReceiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.dmtReportReceipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _dmtReportReceiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _dmtReportReceiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //AEPS Report Receipt
    private val _aepsReportReceiptReceiptResponseLiveData =
        MutableLiveData<ResponseState<AEPSReportModel>>()
    val aepsReportReceiptReceiptResponseLiveData: MutableLiveData<ResponseState<AEPSReportModel>>
        get() = _aepsReportReceiptReceiptResponseLiveData

    suspend fun aepsReportReceiptReceipt(token: String, loginModel: String) {
        _aepsReportReceiptReceiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.aepsReportReceipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _aepsReportReceiptReceiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _aepsReportReceiptReceiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    /*//Check service
    private val _checkServiceResponseLiveData =
        MutableLiveData<ResponseState<CheckServiceModel>>()
    val checkServiceResponseLiveData: MutableLiveData<ResponseState<CheckServiceModel>>
        get() = _checkServiceResponseLiveData

    suspend fun checkService(token: String, loginModel: String) {
        _checkServiceResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.checkService(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _checkServiceResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _checkServiceResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }*/


    /* //Check service home
     private val _checkServiceHomeResponseLiveData =
         MutableLiveData<ResponseState<CheckServiceModel>>()
     val checkServiceHomeResponseLiveData: MutableLiveData<ResponseState<CheckServiceModel>>
         get() = _checkServiceHomeResponseLiveData

     suspend fun checkServiceHome(token: String, loginModel: String) {
       //  _checkServiceHomeResponseLiveData.postValue(ResponseState.Loading())
         try {
        //     val response = api.checkServiceHomePage(
          //       token,
          //       loginModel.replace("\n", "").replace("\r", "")
             )
             _checkServiceHomeResponseLiveData.postValue(
                 ResponseState.create(
                     response,
                     "aa"
                 )
             )
         } catch (throwable: Throwable) {
             Log.d("TAG_service", "checkServiceHome: "+throwable.message)
             _checkServiceHomeResponseLiveData.postValue(ResponseState.create(throwable))
         }

     }
 */


    //MATM
    private val _microatm_report_receiptResponseReceptLiveData =
        MutableLiveData<ResponseState<MatmeportModel>>()
    val microatm_report_receiptResponseReceptLiveData: MutableLiveData<ResponseState<MatmeportModel>>
        get() = _microatm_report_receiptResponseReceptLiveData

    suspend fun microatmReportReceipt(token: String, loginModel: String) {
        _microatm_report_receiptResponseReceptLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.matmREportRECEPT(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _microatm_report_receiptResponseReceptLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _microatm_report_receiptResponseReceptLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //ResetTPIN
    private val _resetTPINResponseReceptLiveData =
        MutableLiveData<ResponseState<ResetTPINModel>>()
    val resetTPINResponseReceptLiveData: MutableLiveData<ResponseState<ResetTPINModel>>
        get() = _resetTPINResponseReceptLiveData

    suspend fun resetTPIN(token: String, loginModel: String) {
        _resetTPINResponseReceptLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.resetTPIN(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _resetTPINResponseReceptLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _resetTPINResponseReceptLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //patternlogin
    private val _patternLoginModelReceptLiveData =
        MutableLiveData<ResponseState<PatternLoginModel>>()
    val patternLoginModelReceptLiveData: MutableLiveData<ResponseState<PatternLoginModel>>
        get() = _patternLoginModelReceptLiveData

    suspend fun patternLogin(token: String, loginModel: String) {
        _patternLoginModelReceptLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.patternlogin(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _patternLoginModelReceptLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _patternLoginModelReceptLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //move To Bank
    private val _moveToBankReceptLiveData =
        MutableLiveData<ResponseState<MoveToBankBankListModel>>()
    val moveToBankReceptLiveData: MutableLiveData<ResponseState<MoveToBankBankListModel>>
        get() = _moveToBankReceptLiveData

    suspend fun moveToBank(token: String, loginModel: String) {
        _moveToBankReceptLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.moveToBank(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _moveToBankReceptLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _moveToBankReceptLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //submit move To Bank
    private val _submit_moveToBankReceptLiveData =
        MutableLiveData<ResponseState<SubmitMoveToBankBankListModel>>()
    val submit_moveToBankReceptLiveData: MutableLiveData<ResponseState<SubmitMoveToBankBankListModel>>
        get() = _submit_moveToBankReceptLiveData

    suspend fun submitMovetobank(token: String, loginModel: String) {
        _submit_moveToBankReceptLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.submitMovetobank(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _submit_moveToBankReceptLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _submit_moveToBankReceptLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //add Bank
    private val _addToBankReceptLiveData =
        MutableLiveData<ResponseState<AddBankModel>>()
    val addToBankReceptLiveData: MutableLiveData<ResponseState<AddBankModel>>
        get() = _addToBankReceptLiveData

    suspend fun addToBank(token: String, loginModel: String, imagedata: MultipartBody.Part?) {
        _addToBankReceptLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.addBank(
                token,
                loginModel.replace("\n", "").replace("\r", "").replace("\r", "")
                    .toRequestBody("text/plain".toMediaTypeOrNull()), imagedata
            )
            _addToBankReceptLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            Log.d("TAG_throwable", "addToBank: " + throwable.message)
            _addToBankReceptLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Move to wallet
    private val _moveToWalletLiveData =
        MutableLiveData<ResponseState<MoveToWalletModel>>()
    val moveToWalletLiveData: MutableLiveData<ResponseState<MoveToWalletModel>>
        get() = _moveToWalletLiveData

    suspend fun moveToWallet(token: String, loginModel: String) {
        _moveToWalletLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.moveToWallet(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _moveToWalletLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _moveToWalletLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Submit Move to wallet
    private val _submitMoveToWalletLiveData =
        MutableLiveData<ResponseState<SubmitMoveToBankBankListModel>>()
    val submitMoveToWalletLiveData: MutableLiveData<ResponseState<SubmitMoveToBankBankListModel>>
        get() = _submitMoveToWalletLiveData

    suspend fun submitMoveToWallet(token: String, loginModel: String) {
        _submitMoveToWalletLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.submitMoveToWallet(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _submitMoveToWalletLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _submitMoveToWalletLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //onboarding Basicinfo
    private val _onboardingBasicinfoResponseLiveData =
        MutableLiveData<ResponseState<BasicInfo>>()
    val onboardingBasicinfoResponseLiveData: LiveData<ResponseState<BasicInfo>>
        get() = _onboardingBasicinfoResponseLiveData


    suspend fun onboardingBasicinfo(token: String, loginModel: String) {
        _onboardingBasicinfoResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.onboardingBasicinfo(token, loginModel.replace("\n", "").replace("\r", ""))
            _onboardingBasicinfoResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _onboardingBasicinfoResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //onboarding Basicinfo22
    /*private val _onboardingBasicinfoResponseLiveData =
        MutableLiveData<ResponseState<BasicInfo>>()
    val onboardingBasicinfoResponseLiveData: LiveData<ResponseState<BasicInfo>>
        get() = _onboardingBasicinfoResponseLiveData*/


    suspend fun onboardingBasicinfo2(
        token: String,
        loginModel: String,
        panimagedata: MultipartBody.Part?,
        aadharfrontimagedata: MultipartBody.Part?,
        aadharbackimagedata: MultipartBody.Part?
    ) {
        _onboardingBasicinfoResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.onboardingBasicinfo2(
                    token,
                    loginModel.replace("\n", "").replace("\r", "")
                        .toRequestBody("text/plain".toMediaTypeOrNull()),
                    panimagedata,
                    aadharfrontimagedata,
                    aadharbackimagedata
                )
            _onboardingBasicinfoResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _onboardingBasicinfoResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    suspend fun onboardingBasicinfo(
        token: String,
        jsonData: String,
        image1Base64: String?,
        image2Base64: String?,
        image3Base64: String?
    ) {
        try {
            val jsonRequestBody = jsonData.toRequestBody("application/json".toMediaTypeOrNull())

            val image1Part = image1Base64?.let {
                val requestBody = it.toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image1", "image1.jpg", requestBody)
            }

            val image2Part = image2Base64?.let {
                val requestBody = it.toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image2", "image2.jpg", requestBody)
            }

            val image3Part = image3Base64?.let {
                val requestBody = it.toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image3", "image3.jpg", requestBody)
            }

            val response =
                api.onboardingBasicinfo(token, jsonRequestBody, image1Part, image2Part, image3Part)
            // Handle response
        } catch (throwable: Throwable) {
            // Handle error
        }
    }

    //StateList
    private val _StateListResponseLiveData =
        MutableLiveData<ResponseState<StateListModel>>()
    val StateListResponseLiveData: LiveData<ResponseState<StateListModel>>
        get() = _StateListResponseLiveData


    suspend fun StateList(token: String, loginModel: String) {
        _StateListResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.StateList(token, loginModel.replace("\n", "").replace("\r", ""))
            _StateListResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _StateListResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //CityList
    private val _CityListResponseLiveData =
        MutableLiveData<ResponseState<CityListModel>>()
    val CityListResponseLiveData: LiveData<ResponseState<CityListModel>>
        get() = _CityListResponseLiveData


    suspend fun CityList(token: String, loginModel: String) {
        _CityListResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.CityList(token, loginModel.replace("\n", "").replace("\r", ""))
            _CityListResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _CityListResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //businesstype
    private val _businesstypeResponseLiveData =
        MutableLiveData<ResponseState<businesstypeMethod>>()
    val businesstypeResponseLiveData: LiveData<ResponseState<businesstypeMethod>>
        get() = _businesstypeResponseLiveData


    suspend fun businesstype(token: String, loginModel: String) {
        _businesstypeResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.businesstype(token, loginModel.replace("\n", "").replace("\r", ""))
            _businesstypeResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _businesstypeResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //businesstypeMethod
    private val _businesstypeMethodResponseLiveData =
        MutableLiveData<ResponseState<businessCategoryModel>>()
    val businesstypeMethodResponseLiveData: LiveData<ResponseState<businessCategoryModel>>
        get() = _businesstypeMethodResponseLiveData


    suspend fun businesstypeMethod(token: String, loginModel: String) {
        _businesstypeMethodResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.businesstypeMethod(token, loginModel.replace("\n", "").replace("\r", ""))
            _businesstypeMethodResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _businesstypeMethodResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //businesscategoryMethod
    private val _businesscategoryMethodResponseLiveData =
        MutableLiveData<ResponseState<businessCategoryModel>>()
    val businesscategoryMethodResponseLiveData: LiveData<ResponseState<businessCategoryModel>>
        get() = _businesscategoryMethodResponseLiveData


    suspend fun businesscategoryMethod(token: String, loginModel: String) {
        _businesscategoryMethodResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.businesscategoryMethod(token, loginModel.replace("\n", "").replace("\r", ""))
            _businesscategoryMethodResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _businesscategoryMethodResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //companyDetailsMethod
    private val _companyDetailsMethodResponseLiveData =
        MutableLiveData<ResponseState<companyDetailsModel>>()
    val companyDetailsMethodResponseLiveData: LiveData<ResponseState<companyDetailsModel>>
        get() = _companyDetailsMethodResponseLiveData


    suspend fun companyDetailsMethod(token: String, loginModel: String) {
        _companyDetailsMethodResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.companyDetailsMethod(token, loginModel.replace("\n", "").replace("\r", ""))
            _companyDetailsMethodResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _companyDetailsMethodResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //bankname
    private val _banknameResponseLiveData =
        MutableLiveData<ResponseState<banknameModel>>()
    val banknameResponseLiveData: LiveData<ResponseState<banknameModel>>
        get() = _banknameResponseLiveData


    suspend fun bankname(token: String, loginModel: String) {
        _banknameResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.bankname(token, loginModel.replace("\n", "").replace("\r", ""))
            _banknameResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _banknameResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //bankDetails
    private val _bankDetailsResponseLiveData =
        MutableLiveData<ResponseState<bankDetailsModel>>()
    val bankDetailsResponseLiveData: LiveData<ResponseState<bankDetailsModel>>
        get() = _bankDetailsResponseLiveData


    suspend fun bankDetails(token: String, loginModel: String, panimagedata: MultipartBody.Part?) {
        _bankDetailsResponseLiveData.postValue(ResponseState.Loading())
        try {
            /* val response =
                 api.bankDetails(token, loginModel.replace("\n", "").replace("\r", "").toRequestBody("text/plain".toMediaTypeOrNull(),panimagedata))*/

            val response =
                api.bankDetails(
                    token,
                    loginModel.replace("\n", "").replace("\r", "")
                        .toRequestBody("text/plain".toMediaTypeOrNull()),
                    panimagedata
                )

            _bankDetailsResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _bankDetailsResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //onboarding Basicinfo
    private val _documentUploadResponseLiveData =
        MutableLiveData<ResponseState<BasicInfo>>()
    val documentUploadResponseLiveData: LiveData<ResponseState<BasicInfo>>
        get() = _documentUploadResponseLiveData

    suspend fun documentUpload(
        token: String,
        loginModel: String,
        partnerPanCard: MultipartBody.Part?,
        companyPanCard: MultipartBody.Part?,
        partnerAadhaarFront: MultipartBody.Part?,
        partnerAadhaarBack: MultipartBody.Part?,
        gstin: MultipartBody.Part?,
        coi: MultipartBody.Part?,
        boardResolution: MultipartBody.Part?,
        tradeLicense: MultipartBody.Part?,
        userSelfi: MultipartBody.Part?,
        userScp: MultipartBody.Part?,
        videoKyc: MultipartBody.Part?,
    ) {
        _documentUploadResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.documentUpload(
                    token,
                    loginModel.replace("\n", "").replace("\r", "")
                        .toRequestBody("text/plain".toMediaTypeOrNull()),
                    partnerPanCard,
                    companyPanCard,
                    partnerAadhaarFront,
                    partnerAadhaarBack,
                    gstin,
                    coi,
                    boardResolution,
                    tradeLicense,
                    userSelfi,
                    userScp,
                    videoKyc
                )
            _documentUploadResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _documentUploadResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //bankList
    private val _bankListResponseLiveData =
        MutableLiveData<ResponseState<AllBankListModel>>()
    val bankListResponseLiveData: LiveData<ResponseState<AllBankListModel>>
        get() = _bankListResponseLiveData


    suspend fun bankList(token: String, loginModel: String) {
        _bankListResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.bankList(token, loginModel.replace("\n", "").replace("\r", ""))
            _bankListResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _bankListResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //PaymentREquistMode
    private val _PaymentREquistModeResponseLiveData =
        MutableLiveData<ResponseState<PaymentREquistModeModel>>()
    val PaymentREquistModeResponseLiveData: LiveData<ResponseState<PaymentREquistModeModel>>
        get() = _PaymentREquistModeResponseLiveData


    suspend fun PaymentREquistMode(token: String, loginModel: String) {
        _PaymentREquistModeResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.PaymentREquistMode(token, loginModel.replace("\n", "").replace("\r", ""))
            _PaymentREquistModeResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _PaymentREquistModeResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //PaymentRequist
    private val _PaymentRequistResponseLiveData =
        MutableLiveData<ResponseState<PaymentRequistModel>>()
    val PaymentRequistResponseLiveData: LiveData<ResponseState<PaymentRequistModel>>
        get() = _PaymentRequistResponseLiveData


    suspend fun PaymentRequist(
        token: String,
        loginModel: String,
        paymentSlip: MultipartBody.Part?,
        denomSlip: MultipartBody.Part?
    ) {
        _PaymentRequistResponseLiveData.postValue(ResponseState.Loading())
        try {
            /* val response =
                 api.PaymentRequist(token, loginModel.replace("\n", "").replace("\r", ""))*/
            val response =
                api.PaymentRequist(
                    token,
                    loginModel.replace("\n", "").replace("\r", "")
                        .toRequestBody("text/plain".toMediaTypeOrNull()),
                    paymentSlip,
                    denomSlip
                )
            _PaymentRequistResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _PaymentRequistResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //ServiceCheck
    private val _ServiceCheckResponseLiveData =
        MutableLiveData<ResponseState<ServiceCheckModel>>()
    val ServiceCheckResponseLiveData: MutableLiveData<ResponseState<ServiceCheckModel>>
        get() = _ServiceCheckResponseLiveData


    suspend fun ServiceCheck(token: String, loginModel: String) {
        _ServiceCheckResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.ServiceCheck(token, loginModel.replace("\n", "").replace("\r", ""))
            _ServiceCheckResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _ServiceCheckResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //ServiceCheckViewMore
    private val _ServiceCheckViewMoreResponseLiveData =
        MutableLiveData<ResponseState<ServiceCheckModel>>()
    val ServiceCheckViewMoreResponseLiveData: MutableLiveData<ResponseState<ServiceCheckModel>>
        get() = _ServiceCheckViewMoreResponseLiveData


    suspend fun ServiceCheckViewMore(token: String, loginModel: String) {
        _ServiceCheckViewMoreResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.ServiceCheckViewMore(token, loginModel.replace("\n", "").replace("\r", ""))
            _ServiceCheckViewMoreResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _ServiceCheckViewMoreResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //addBankBankList
    private val _addBankBankListResponseLiveData =
        MutableLiveData<ResponseState<AddBankBankListModel>>()
    val addBankBankListResponseLiveData: LiveData<ResponseState<AddBankBankListModel>>
        get() = _addBankBankListResponseLiveData


    suspend fun addBankBankList(token: String, loginModel: String) {
        _addBankBankListResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.addBankBankList(token, loginModel.replace("\n", "").replace("\r", ""))
            _addBankBankListResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _addBankBankListResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Forgot password
    private val _forgotPasswordResponseLiveData =
        MutableLiveData<ResponseState<ForgotPasswordModel>>()
    val forgotPasswordResponseLiveData: MutableLiveData<ResponseState<ForgotPasswordModel>>
        get() = _forgotPasswordResponseLiveData


    suspend fun forgotPassword(token: String, loginModel: String) {
        _forgotPasswordResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.ForgotPassword(token, loginModel.replace("\n", "").replace("\r", ""))
            _forgotPasswordResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _forgotPasswordResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //forgot password veify otp
    private val _forgotPasswordVerifyOtpResponseLiveData =
        MutableLiveData<ResponseState<ForgotPasswordVerifyOtpModel>>()
    val forgotPasswordVerifyOtpResponseLiveData: MutableLiveData<ResponseState<ForgotPasswordVerifyOtpModel>>
        get() = _forgotPasswordVerifyOtpResponseLiveData


    suspend fun ForgotPasswordVerifyOtp(token: String, loginModel: String) {
        _forgotPasswordVerifyOtpResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.ForgotPasswordVerifyOtp(token, loginModel.replace("\n", "").replace("\r", ""))
            _forgotPasswordVerifyOtpResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _forgotPasswordVerifyOtpResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //refreshToken
    private val _refreshTokenResponseLiveData =
        MutableLiveData<ResponseState<refreshTokenModel>>()
    val refreshTokenResponseLiveData: LiveData<ResponseState<refreshTokenModel>>
        get() = _refreshTokenResponseLiveData


    suspend fun refreshToken(token: String, loginModel: String) {
        _refreshTokenResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.refreshToken(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _refreshTokenResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _refreshTokenResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //app Update
    private val _appUpdateResponseLiveData =
        MutableLiveData<ResponseState<appUpdateUrlModel>>()
    val appUpdateResponseLiveData: MutableLiveData<ResponseState<appUpdateUrlModel>>
        get() = _appUpdateResponseLiveData


    suspend fun appUpdate(token: String, loginModel: String) {
        _appUpdateResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.appupdate(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _appUpdateResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _appUpdateResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }





    //cashCollection
    private val _cashCollectionResponseLiveData =
        MutableLiveData<ResponseState<cashCollectionModel>>()
    val cashCollectionResponseLiveData: MutableLiveData<ResponseState<cashCollectionModel>>
        get() = _cashCollectionResponseLiveData


    suspend fun cashCollection(
        token: String,
        loginModel: String,
        insuranceOrCashCollection: String
    ) {
        _cashCollectionResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.cashCollection(
                token, loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                    .replace("\\s".toRegex(), "".trim())
            )

            _cashCollectionResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _cashCollectionResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //insurance
    private val _insuranceResponseLiveData =
        MutableLiveData<ResponseState<InsuranceModel>>()
    val insuranceResponseLiveData: MutableLiveData<ResponseState<InsuranceModel>>
        get() = _insuranceResponseLiveData

    suspend fun insurance(
        token: String,
        loginModel: String,
        insuranceOrCashCollection: String
    ) {
        _insuranceResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.insurance(
                token, loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                    .replace("\\s".toRegex(), "".trim())
            )

            _insuranceResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _insuranceResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //checkUser
    private val _checkUserResponseLiveData =
        MutableLiveData<ResponseState<checkUserModel>>()
    val checkUserResponseLiveData: MutableLiveData<ResponseState<checkUserModel>>
        get() = _checkUserResponseLiveData


    suspend fun checkUser(token: String, loginModel: String) {
        _checkUserResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.checkUser(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _checkUserResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _checkUserResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //checkUser2
    private val _checkUserResponseLiveData2 =
        MutableLiveData<ResponseState<checkUserModel>>()
    val checkUserResponseLiveData2: MutableLiveData<ResponseState<checkUserModel>>
        get() = _checkUserResponseLiveData2


    suspend fun checkUser2(token: String, loginModel: String) {
        _checkUserResponseLiveData2.postValue(ResponseState.Loading())
        try {
            val response =
                api.checkUser2(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _checkUserResponseLiveData2.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _checkUserResponseLiveData2.postValue(ResponseState.create(throwable))
        }

    }



    //beneficiaryList
    private val _beneficiaryListResponseLiveData =
        MutableLiveData<ResponseState<beneficiaryListModel>>()
    val beneficiaryListResponseLiveData: MutableLiveData<ResponseState<beneficiaryListModel>>
        get() = _beneficiaryListResponseLiveData


    suspend fun beneficiaryList(token: String, loginModel: String) {
        _beneficiaryListResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.beneficiaryList(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _beneficiaryListResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _beneficiaryListResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //addRemitter
    private val _addRemitterResponseLiveData =
        MutableLiveData<ResponseState<addRemitterModel>>()
    val addRemitterResponseLiveData: MutableLiveData<ResponseState<addRemitterModel>>
        get() = _addRemitterResponseLiveData


    suspend fun addRemitter(token: String, loginModel: String) {
        _addRemitterResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.addRemitter(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _addRemitterResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _addRemitterResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //addBeneficiary
    private val _addBeneficiaryResponseLiveData =
        MutableLiveData<ResponseState<addBeneficiaryModel>>()
    val addBeneficiaryResponseLiveData: MutableLiveData<ResponseState<addBeneficiaryModel>>
        get() = _addBeneficiaryResponseLiveData


    suspend fun addBeneficiary(token: String, loginModel: String) {
        _addBeneficiaryResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.addBeneficiary(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _addBeneficiaryResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _addBeneficiaryResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //beneficiary Verify
    private val _beneficiaryVerifyResponseLiveData =
        MutableLiveData<ResponseState<verifyBeneficiaryModel>>()
    val beneficiaryVerifyResponseLiveData: MutableLiveData<ResponseState<verifyBeneficiaryModel>>
        get() = _beneficiaryVerifyResponseLiveData


    suspend fun beneficiaryVerify(token: String, loginModel: String) {
        _beneficiaryVerifyResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.beneficiaryVerify(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _beneficiaryVerifyResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _beneficiaryVerifyResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //moneyTransfer
    private val _moneyTransferResponseLiveData =
        MutableLiveData<ResponseState<MoneyTranspherModel>>()
    val moneyTransferResponseLiveData: MutableLiveData<ResponseState<MoneyTranspherModel>>
        get() = _moneyTransferResponseLiveData


    suspend fun moneyTransfer(token: String, loginModel: String) {
        _moneyTransferResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.moneyTransfer(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _moneyTransferResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _moneyTransferResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //electricStatelist
    private val _electricStatelistResponseLiveData =
        MutableLiveData<ResponseState<electricStatelistModel>>()
    val electricStatelistResponseLiveData: MutableLiveData<ResponseState<electricStatelistModel>>
        get() = _electricStatelistResponseLiveData


    suspend fun electricStatelist(token: String, loginModel: String) {
        _electricStatelistResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.electricStatelist(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _electricStatelistResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _electricStatelistResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //electricbillerlist
    private val _electricbillerlistResponseLiveData =
        MutableLiveData<ResponseState<billerlistModel>>()
    val electricbillerlistResponseLiveData: MutableLiveData<ResponseState<billerlistModel>>
        get() = _electricbillerlistResponseLiveData


    suspend fun electricbillerlist(token: String, loginModel: String) {
        _electricbillerlistResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.electricbillerlist(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _electricbillerlistResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _electricbillerlistResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //electricBillbillFetch
    private val _electricBillbillFetchResponseLiveData =
        MutableLiveData<ResponseState<electricBillbillFetchModel>>()
    val electricBillbillFetchResponseLiveData: MutableLiveData<ResponseState<electricBillbillFetchModel>>
        get() = _electricBillbillFetchResponseLiveData


    suspend fun electricBillbillFetch(token: String, loginModel: String) {
        _electricBillbillFetchResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.electricBillbillFetch(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _electricBillbillFetchResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _electricBillbillFetchResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //billpaytransaction
    private val _billpaytransactionResponseLiveData =
        MutableLiveData<ResponseState<billpaytransactionModel>>()
    val billpaytransactionResponseLiveData: MutableLiveData<ResponseState<billpaytransactionModel>>
        get() = _billpaytransactionResponseLiveData


    suspend fun billpaytransaction(token: String, loginModel: String) {
        _billpaytransactionResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.billpaytransaction(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _billpaytransactionResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _billpaytransactionResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }
    //billpaytransaction
    private val _addRetailerResponseLiveData =
        MutableLiveData<ResponseState<AddretailerModel>>()
    val addRetailerResponseLiveData: MutableLiveData<ResponseState<AddretailerModel>>
        get() = _addRetailerResponseLiveData


    suspend fun addRetailer(token: String, loginModel: String) {
        _addRetailerResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.addRetailer(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _addRetailerResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _addRetailerResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //view retailer
    private val _viewRetailerModelResponseLiveData =
        MutableLiveData<ResponseState<ViewRetailerModel>>()
    val viewRetailerModelResponseLiveData: MutableLiveData<ResponseState<ViewRetailerModel>>
        get() = _viewRetailerModelResponseLiveData


    suspend fun ViewRetailerModel(token: String, loginModel: String) {
        _viewRetailerModelResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.ViewRetailerModel(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _viewRetailerModelResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _viewRetailerModelResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }
    //add_retailer_dtls
    private val _add_retailer_dtlsResponseLiveData =
        MutableLiveData<ResponseState<AddRetailarDetailsModel>>()
    val add_retailer_dtlsResponseLiveData: MutableLiveData<ResponseState<AddRetailarDetailsModel>>
        get() = _add_retailer_dtlsResponseLiveData


    suspend fun add_retailer_dtls(token: String, loginModel: String) {
        _add_retailer_dtlsResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.add_retailer_dtls(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _add_retailer_dtlsResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _add_retailer_dtlsResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

   //pay_partner
    private val _pay_partnerResponseLiveData =
        MutableLiveData<ResponseState<PayPartnerModel>>()
    val pay_partnerResponseLiveData: MutableLiveData<ResponseState<PayPartnerModel>>
        get() = _pay_partnerResponseLiveData


    suspend fun pay_partner(token: String, loginModel: String) {
        _pay_partnerResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.pay_partner(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _pay_partnerResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _pay_partnerResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //check merchant
    private val _checkmerchantResponseLiveData =
        MutableLiveData<ResponseState<CheckMerchant>>()
    val checkmerchantResponseLiveData: MutableLiveData<ResponseState<CheckMerchant>>
        get() = _checkmerchantResponseLiveData


    suspend fun checkmerchant(token: String, loginModel: String) {
        _checkmerchantResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.checkMerchant(
                    token,
                    loginModel.replace("\\n", "").replace("\\r", "").replace("\n", " ")
                        .replace("\\s".toRegex(), "".trim())
                )
            _checkmerchantResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _checkmerchantResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }




}


package com.big9.app.network

import AddRetailarDetailsModel
import AddretailerModel
import InsuranceModel
import MoneyTranspherModel
import PayPartnerModel
import ViewRetailerModel
import addBeneficiaryModel
import addRemitterModel
import appUpdateUrlModel
import beneficiaryListModel
import billerlistModel
import billpaytransactionModel
import cashCollectionModel
import checkUserModel
import com.big9.app.data.model.sample.Test
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
import com.big9.app.data.model.allReport.receipt.Dmt_report_receiptModel
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
import com.google.gson.JsonObject
import electricBillbillFetchModel
import electricStatelistModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import refreshTokenModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import verifyBeneficiaryModel

interface RetroApi {

    /*{ "response": { "data": [ { "name":"Test User 1" }, { "name":"Test User 2" } ], "status": { "msg": "Sample message.", "action_status": false }, "publish": { "version": "Api.0.0.0", "developer": "bdas" } } }*/
    @POST("abcd.php")
    suspend fun login(@Body loginRequest: JsonObject): Response<BaseResponse<Test>>//ResponseState<Test>

    @POST("form.php")
    suspend fun formReg(@Body regModel: RegForm): Response<BaseResponse<Test>>//ResponseState<Test>

    //
    @POST("form_doc.php")
    suspend fun docUpload(@Body documentUploadModel: DocumentUploadModel): Response<BaseResponse<Test>>

    /* @POST("auth")
     suspend fun epayLogin(@Body authData: String): Response<BaseResponse<Test>>*/

    @FormUrlEncoded
    @POST("auth")
    suspend fun epayLogin(
        @Header("Authorize") header: String,
        @Field("authData") authData: String
    ): Response<BaseResponse<LoginResponse>>


    @POST("otpverify")
    suspend fun otpverify(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<OtpResponse>

    @POST("v1/users/profile")
    suspend fun profile2(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<profileResponse>


    @POST("v1/users/profile")
    suspend fun profile(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<profileResponse>

    @POST("v1/users/profile")
    suspend fun testing(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<OtpResponse>


    @POST("v1/reports/payment_report")
    suspend fun paymentReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PaymentReportResponse>

    @POST("v1/reports/transcation_report")
    suspend fun transcationReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<TransactionReportResponse>

    @POST("v1/reports/dmt_report")
    suspend fun dmtReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<DmtReportReportModel>

    @POST("v1/reports/load_request_report")
    suspend fun loadRequestReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<loadRequestModel>

    @POST("v1/reports/wallet_ledger_report")
    suspend fun walletLedgerReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<WalletLedgerModel>

    @POST("v1/reports/aeps_report")
    suspend fun aepsReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<aepsReportModel>

    @POST("v1/reports/microatm_report")
    suspend fun microatmReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<MicroatmReportModel>

    @POST("v1/reports/commission_report")
    suspend fun commissionReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<commissionReportModel>

    /* @POST("v1/reports/complaints_report")
    suspend fun complaints_report(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<complaints_reportMode>*/

    @POST("v1/reports/complaints_report")
    suspend fun complaints_report(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<complaints_reportMode>

    @POST("v1/reports/wallet_settle_report")
    suspend fun walletSettleReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<WalletSettleReportModel>

    @POST("v1/reports/bank_settle_report")
    suspend fun bank_settle_report(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Bank_settle_reportModel>

    @POST("v1/reports/cashout_ledger_report")
    suspend fun cashout_ledger_report(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Cashout_ledger_reportModel>

    @POST("v1/reports/transcation_report_receipt")
    suspend fun transcation_report_receipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Transcation_report_receiptReportModel>

    @POST("v1/reports/dmt_report_receipt")
    suspend fun dmt_report_receipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Dmt_report_receiptModel>

    @POST("v1/reports/microatm_report_receipt")
    suspend fun microatm_report_receipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Microatm_report_receipt>

    @POST("v1/reports/aeps_report_receipt")
    suspend fun aeps_report_receipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Dmt_report_receiptModel>

    @POST("v1/services/operatorlist")
    suspend fun MobilePostPaidOperatorList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PostPaidMobileOperatorListModel>

    @POST("v1/services/mobile/post_transfer")
    suspend fun PostPaidMobileTranspher(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PostPaidMobileTranspherModel>


    @POST("v1/services/operatorlist")
    suspend fun MobilePrePaidOperatorList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PrePaidMobileOperatorListModel>

    @POST("v1/services/mrcplan")
    suspend fun MobilePrePaidPlainList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PrepaidMobolePlainModel>



    @POST("v1/services/mobile/pre_transfer")
    suspend fun MobilePrePaidpreTransfer(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PrepaidMoboleTranspherModel>


    @POST("v1/services/creditcard/send_otp")
    suspend fun creditCardSendOtp(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<CreditCardSendOtpModel>


    @POST("v1/services/creditcard/verify_otp")
    suspend fun creditCardverifyOtp(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<CreditCardVerifyOtpModel>


    @POST("v1/services/epotly/epotly_transfer")
    suspend fun epotlyTransfer(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<EPotlyTranspherModel>


    @POST("v1/services/dth/transfer")
    suspend fun dthTransfer(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<DTHTranspherModel>


    @POST("v1/services/dth/info")
    suspend fun dthUserInfo(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<DTHUserInfoModel>


    @POST("v1/password/change_pin")
    suspend fun changePin(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ChangeUserPasswordModel>

    @POST("v1/password/change_tpin")
    suspend fun changeTpin(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ChangeUserTPINPasswordModel>

    @POST("v1/reports/transcation_report_receipt")
    suspend fun transcationReportReceipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<TransactionReportModel>

    @POST("v1/reports/dmt_report_receipt")
    suspend fun dmtReportReceipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<DMTReportModel>

    @POST("v1/reports/aeps_report_receipt")
    suspend fun aepsReportReceipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<AEPSReportModel>

    /* @POST("v1/check-service")
    suspend fun checkService(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<CheckServiceModel>

    @POST("v1/check-service")
    suspend fun checkServiceHomePage(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<CheckServiceModel>
*/

    @POST("v1/check-service")
    suspend fun ServiceCheck(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ServiceCheckModel>

    @POST("v1/check-service")
    suspend fun ServiceCheckViewMore(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ServiceCheckModel>

    @POST("v1/reports/microatm_report_receipt")
    suspend fun matmREportRECEPT(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<MatmeportModel>


    @POST("v1/password/reset_tpin")
    suspend fun resetTPIN(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ResetTPINModel>

    @POST("v1/password/patternlogin")
    suspend fun patternlogin(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PatternLoginModel>


    @POST("v1/services/mtb/list")
    suspend fun moveToBank(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<MoveToBankBankListModel>

    @POST("v1/services/mtb/transfer")
    suspend fun submitMovetobank(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<SubmitMoveToBankBankListModel>

    /*@Multipart
    @POST("v1/services/mtb/add_bank")
    suspend fun addBank(
        @Header("Authtoken") token: String,
        @Body data: String,
        imagedata: MultipartBody.Part?
    ): Response<AddBankModel>*/

    @Multipart
    @POST("v1/services/mtb/add_bank")
    suspend fun addBank(
        @Header("Authtoken") token: String,
        @Part("data") data: RequestBody,
        @Part imagedata: MultipartBody.Part?
    ): Response<AddBankModel>

    @POST("v1/services/mtw/stp")
    suspend fun moveToWallet(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<MoveToWalletModel>

    @POST("v1/services/mtw/stw")
    suspend fun submitMoveToWallet(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<SubmitMoveToBankBankListModel>


    @POST("v1/onboarding/basicinfo")
    suspend fun onboardingBasicinfo(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<BasicInfo>

    @Multipart
    @POST("v1/onboarding/basicinfo")
    suspend fun onboardingBasicinfo2(
        @Header("Authtoken") token: String,
        @Part("data") data: RequestBody,
        @Part image1: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?
    ): Response<BasicInfo>


    @Multipart
    @POST("v1/onboarding/basicinfo")
    suspend fun onboardingBasicinfo(
        @Header("Authtoken") token: String,
        @Part("data") data: RequestBody,
        @Part image1: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?
    ): Response<BasicInfo>


    @POST("v1/services/statelist")
    suspend fun StateList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<StateListModel>

    @POST("v1/services/districtlist")
    suspend fun CityList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<CityListModel>

    @POST("v1/services/businesstype")
    suspend fun businesstype(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<businesstypeMethod>

    @POST("v1/services/businesscategory")
    suspend fun businesstypeMethod(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<businessCategoryModel>

    @POST("v1/services/businesscategory")
    suspend fun businesscategoryMethod(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<businessCategoryModel>

    @POST("v1/onboarding/company_details")
    suspend fun companyDetailsMethod(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<companyDetailsModel>


    @POST("v1/services/bankname")
    suspend fun bankname(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<banknameModel>

    /*@POST("v1/onboarding/bank_details")
    suspend fun bankDetails(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<bankDetailsModel>*/


    @Multipart
    @POST("v1/onboarding/bank_details")
    suspend fun bankDetails(
        @Header("Authtoken") token: String,
        @Part("data") data: RequestBody,
        @Part image1: MultipartBody.Part?
    ): Response<bankDetailsModel>


    @Multipart
    @POST("v1/onboarding/document_upload")
    suspend fun documentUpload(
        @Header("Authtoken") token: String,
        @Part("data") data: RequestBody,
        @Part partnerPanCard: MultipartBody.Part?,
        @Part companyPanCard: MultipartBody.Part?,
        @Part partnerAadhaarFront: MultipartBody.Part?,
        @Part partnerAadhaarBack: MultipartBody.Part?,
        @Part gstin: MultipartBody.Part?,
        @Part coi: MultipartBody.Part?,
        @Part boardResolution: MultipartBody.Part?,
        @Part tradeLicense: MultipartBody.Part?,
        @Part userSelfi: MultipartBody.Part?,
        @Part userScp: MultipartBody.Part?,
        @Part videoKyc: MultipartBody.Part?,
    ): Response<BasicInfo>


    @POST("v1/services/pr/list")
    suspend fun bankList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<AllBankListModel>

    @POST("v1/services/pr/mode")
    suspend fun PaymentREquistMode(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PaymentREquistModeModel>

    @Multipart
    @POST("v1/services/pr/form")
    suspend fun PaymentRequist(
        @Header("Authtoken") token: String,
        @Part("data") data: RequestBody,
        @Part image1: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
    ): Response<PaymentRequistModel>

    @POST("v1/services/mtb/bank_list")
    suspend fun addBankBankList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<AddBankBankListModel>

    @POST("v1/password/forgot_pin")
    suspend fun ForgotPassword(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ForgotPasswordModel>


    @POST("v1/password/verify_otp")
    suspend fun ForgotPasswordVerifyOtp(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ForgotPasswordVerifyOtpModel>


    @POST("refreshToken")
    suspend fun refreshToken(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<refreshTokenModel>


    @POST("appupdate")
    suspend fun appupdate(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<appUpdateUrlModel>




    @POST("v1/services/cashcoll/cashcol")
    suspend fun cashCollection(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<cashCollectionModel>

    @POST("v1/services/insurance/insurance.php")
    suspend fun insurance(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<InsuranceModel>


    @POST("v1/services/dmt/check_remitter.php")
    suspend fun checkUser(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<checkUserModel>

    @POST("v1/services/dmt/check_remitter.php")
    suspend fun checkUser2(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<checkUserModel>



    @POST("v1/services/dmt/beneficiary_list.php")
    suspend fun beneficiaryList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<beneficiaryListModel>

    @POST("v1/services/dmt/add_remitter.php")
    suspend fun addRemitter(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<addRemitterModel>

    @POST("v1/services/dmt/add_beneficiary.php")
    suspend fun addBeneficiary(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<addBeneficiaryModel>

    @POST("v1/services/dmt/verify_beneficiary.php")
    suspend fun beneficiaryVerify(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<verifyBeneficiaryModel>

    @POST("v1/services/dmt/money_transfer")
    suspend fun moneyTransfer(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<MoneyTranspherModel>


    @POST("v1/services/billpay/electricbill/statelist.php")
    suspend fun electricStatelist(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<electricStatelistModel>

    @POST("v1/services/billpay/electricbill/billerlist.php")
    suspend fun electricbillerlist(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<billerlistModel>

    @POST("v1/services/billpay/electricbill/billfetch.php")
    suspend fun electricBillbillFetch(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<electricBillbillFetchModel>


    @POST("v1/services/billpay/electricbill/billpay.php")
    suspend fun billpaytransaction(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<billpaytransactionModel>

    @POST("v1/services/channels/add_retailer")
    suspend fun addRetailer(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<AddretailerModel>



    @POST("v1/services/channels/view_partner")
    suspend fun ViewRetailerModel(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ViewRetailerModel>


  @POST("v1/services/channels/add_retailer_dtls")
    suspend fun add_retailer_dtls(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<AddRetailarDetailsModel>


    @POST("v1/services/channels/pay_partner")
    suspend fun pay_partner(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PayPartnerModel>


    @POST("v1/services/aeps/check_merchant")
    suspend fun checkMerchant(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<CheckMerchant>

}
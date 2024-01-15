package com.big9.app.utils.helpers

import android.content.ContentValues
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.adapter.ReportDetailsAdapter
import com.big9.app.adapter.reportAdapter.CommissionReportAdapter
import com.big9.app.adapter.reportAdapter.ReportAdapter

import com.big9.app.data.model.ListIcon
import com.big9.app.data.model.ReceiptModel
import com.big9.app.data.model.ReportModel
import com.big9.app.data.model.Reportdetails
import com.big9.app.data.model.allReport.CommissionReportData

object Constants {
    const val INPUT_FILTER_MAX_VALUE:Long = 9999999999999
    const val INPUT_FILTER_POINTER_LENGTH = 2
    const val LOTTIE_CONFETTIE_LINK = "https://assets10.lottiefiles.com/packages/lf20_3xwxlyv7.json"
    const val LOTTIE_TICK_LINK = "https://assets8.lottiefiles.com/packages/lf20_ikemt7or.json"
    const val CHIPER_CODE = "AES/CBC/PKCS5Padding"
    const val AES_CODE = "PBKDF2WithHmacSHA256"
    const val SECRET_KEY = "AES"
    const val EPAY_SHAREDFREFFRENCE = "EPAY_SHAREDFREFFRENCE"
    const val TEST = "TEST"
    const val ISLogin = "isLogin"
    const val loginData = "loginData"
    const val USER_DATA = "userdata"
    var isDthOperator=false
    var isVideo=false
    var isIsPaySlip=false
    var isDenomination=false
    var isIsCheck=false
    var isPdf=false
    var isBackCamera=true
    var isGallary=false
    var contentValues: ContentValues? =null
    var isCashWithdraw=true
    var searchList = ArrayList<ListIcon>()
    var isFromSearchPage = false
    var isFromUtilityPage = false
    var isRecept = "isRecept"
    var isAfterReg = "isAfterReg"
    var stape = "stape"
    var searchValue = ""
    var searchValueTag = ""
    var utilityValue = ""
    var FCMTOKEN: String="FCMTOKEN"
    var recycleViewReceiptList = ArrayList<ReceiptModel>()
    const val AES_ALGORITHM = "AES"
    const val AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"
    const val AES_KEY =  "a22786308b71488790be222216260e0a"

    const val AES_IV =  "EvUc8IIYzYvcrfXo"
    //const val AES_IV =  "gPmSeGRX2BdOm50EuHW0f9jUHKmfTLtW"
    const val API_KEY =  "677a05e769f1a888ddb86397eb45c57d2700bb7b83b4f3b7282bf6aba4266c7f"
    const val CLIENT_ID =  "big9_164604122023"
    const val Postpaid =  "Postpaid"
    const val Prepaid =  "Prepaid"

    var reportList = ArrayList<ReportModel>()

    var newReportList = ArrayList<ReportModel>()
    var reportList2 = ArrayList<ReportModel>()

    var recyclerView: RecyclerView?=null
    var reportAdapter: ReportAdapter?=null

    var isFirstPageOpeenPostPaidMobile=true


    var commissionReportAdapter: CommissionReportAdapter? = null
    var commissionReportList = ArrayList<CommissionReportData>()
    var commissionReportList2 = ArrayList<CommissionReportData>()

    var reportDetailsPropertyList = ArrayList<Reportdetails>()
    var reportDetailsAdapter: ReportDetailsAdapter?=null
    var loginMobileNumber: String?=null
    var loginMobileReferanceNumber: String?=null
    //var SMS_SENDER_NUMBER: String=""
    var paySleepType=""
    var videoUri: Uri? =null
    var rmnForgotPassword: String? =null
    var customerId: String =""
    var bankCode: String =""

    var eOpid=""
    var appUpdateUrl:String?=null
    var totalBankLimit="a"

    var ID: String=""
    var name: String=""
    var mobileNo: String=""
    var currBalance: String=""
    var bname: String=""
    var epotlyMoboleNo:String=""

}
package com.big9.app.ui.fragment



import ViewRetailerData
import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.big9.app.R
import com.big9.app.adapter.AEPSAdapter
import com.big9.app.adapter.AutoScrollHandler
import com.big9.app.adapter.BankingAdapter
import com.big9.app.adapter.BannerViewpagerAdapter
import com.big9.app.adapter.EssentialAdapter
import com.big9.app.adapter.FinancialAdapter
import com.big9.app.adapter.HorizontalMarginItemDecoration
import com.big9.app.adapter.MostPopularAdapter
import com.big9.app.adapter.ReportAdapter
import com.big9.app.adapter.RetailerAdapter
import com.big9.app.adapter.TravelAdapter
import com.big9.app.adapter.UtilityAdapter
import com.big9.app.adapter.bluetooth.BluetoothDeviceAdapter
import com.big9.app.data.model.ListIcon
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.data.viewMovel.TableViewModel
import com.big9.app.databinding.FragmentHomeBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.CustomPopup.showBalencePopup
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.common.MethodClass.userLogout
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.Postpaid
import com.big9.app.utils.helpers.Constants.Prepaid
import com.big9.app.utils.helpers.Constants.commissionReportAdapter
import com.big9.app.utils.helpers.Constants.commissionReportList
import com.big9.app.utils.helpers.Constants.commissionReportList2
import com.big9.app.utils.helpers.Constants.isCashWithdraw
import com.big9.app.utils.helpers.Constants.isFromSearchPage
import com.big9.app.utils.helpers.Constants.newReportList
import com.big9.app.utils.helpers.Constants.reportAdapter
import com.big9.app.utils.helpers.Constants.reportList
import com.big9.app.utils.helpers.Constants.reportList2
import com.big9.app.utils.helpers.Constants.searchList
import com.big9.app.utils.helpers.Constants.searchValue
import com.big9.app.utils.helpers.Constants.searchValueTag
import com.big9.app.utils.helpers.PermissionUtils
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack2
import com.big9.app.utils.`interface`.CallBack7
import com.big9.app.utils.`interface`.PermissionsCallback
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID


class HomeFragment : BaseFragment() {
    private val tableViewModel: TableViewModel by viewModels()
    private lateinit var bluetoothDevice: BluetoothDevice
    private lateinit var bluetoothSocket: BluetoothSocket
    private lateinit var outputStream: OutputStream
    private lateinit var inputStream: InputStream
    val REQUEST_BLUETOOTH_PERMISSION=3
    var aepsType=""
    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter
    var   bluetoothDeviceAdapter: BluetoothDeviceAdapter?=null
    val permissionsForBloothRequest = mutableListOf<String>(android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_ADMIN)

    private val  MY_PERMISSIONS_REQUEST_CODE=1
    var bluetoothDeviceList: ArrayList<BluetoothDevice> = ArrayList()
    private var serverSocket: BluetoothServerSocket?=null
    private var backPressedTime: Long = 0
    private val backPressedInterval: Long = 2000

    //val permissionList = arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_ADMIN)
    private var isRotated = true
    private var isRotated2 = true
    private var isRotated3 = true
    private var isRotated4 = true
    private var isRotated5 = true
    var iconList = ArrayList<ListIcon>()
    var iconList2 = ArrayList<ListIcon>()
    var iconList3 = ArrayList<ListIcon>()
    var iconList4 = ArrayList<ListIcon>()
    var iconList5 = ArrayList<ListIcon>()
    var iconList6 = ArrayList<ListIcon>()
    var iconList7 = ArrayList<ListIcon>()
    var iconList8 = ArrayList<ListIcon>()
    var iconList9 = ArrayList<ListIcon>()
    var iconList10 = ArrayList<ListIcon>()
    var iconList11 = ArrayList<ListIcon>()
    var iconList12 = ArrayList<ListIcon>()
    var iconList13 = ArrayList<ListIcon>()
    var naviGationValue=""
    var isReport=false
    lateinit var binding: FragmentHomeBinding
    private lateinit var autoScrollHandler: AutoScrollHandler
    private val viewModel: MyViewModel by activityViewModels()
    var deviceHeight:Int=0
    private var loader: Dialog? = null
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this*/

        binding = FragmentHomeBinding.inflate(layoutInflater)
        getDeviceWIDTHandHeight()
        val view = binding.root
        init()
        viewOnClick()
        observer()
        return view
    }

    private fun observer() {
        viewModel.from_page_message.observe(viewLifecycleOwner) {
            if(isFromSearchPage){
                if (searchValue.isNotEmpty() && searchValueTag.isNotEmpty()){

                    serviceNavigation(searchValue,searchValueTag)
                    searchValue=""
                    searchValueTag=""
                }

                isFromSearchPage=false
            }

            viewModel?.ServiceCheckResponseLiveData?.observe(viewLifecycleOwner){
                when (it) {
                    is ResponseState.Loading -> {
                        loader?.show()
                    }

                    is ResponseState.Success -> {
                        loader?.dismiss()
                        it?.data?.slug?.let {slug->
                            serviceNavigation(naviGationValue,slug)
                        }
                        viewModel?.ServiceCheckResponseLiveData?.value=null
                    }

                    is ResponseState.Error -> {
                        loader?.dismiss()
                        //serviceNavigation(naviGationValue,"slug")
                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        viewModel?.ServiceCheckResponseLiveData?.value=null
                    }
                }
            }

            viewModel?.checkmerchantResponseLiveData?.observe(viewLifecycleOwner){
                when (it) {
                    is ResponseState.Loading -> {
                        loader?.show()
                    }

                    is ResponseState.Success -> {
                        loader?.dismiss()
                        isCashWithdraw = true
                        it?.data?.responseCode?.let {responseCode->

                            if (responseCode==209 || responseCode==210 ){
                                handleApiError(it.isNetworkError, 209, it?.data?.Description.toString())
                            }
                            else if (responseCode==211){
                                //AEPS Pay Sprint Onboarding Required
                                isCashWithdraw = false
                                it.data.data?.let {
                                    val bundle = Bundle().apply {
                                        putParcelable("checkMerchantData", it)
                                    }
                                    findNavController().navigate(R.id.action_homeFragment2_to_aadharRegistrationFragment,bundle)
                                }

                                /*when(aepsType){
                                    "balance"->{
                                        findNavController().navigate(R.id.action_homeFragment2_to_balenceAEPSFragment)
                                    }
                                    "withdraw"->{
                                        findNavController().navigate(R.id.action_homeFragment2_to_cashWithdrawFragment)
                                    }
                                    "statement"->{
                                        findNavController().navigate(R.id.action_homeFragment2_to_miniStatementFormFragment)
                                    }
                                    "aadhar_pay"->{
                                        findNavController().navigate(R.id.action_homeFragment2_to_cashWithdrawFragment)
                                    }
                                }*/

                            }
                            else if (responseCode==212){
                                //One Time Registration Required
                                isCashWithdraw = false

                            }
                            else if (responseCode==213){
                                //Two factor verification required
                                isCashWithdraw = false

                            } else {

                            }


                        }

                        viewModel?.checkmerchantResponseLiveData?.value=null
                    }

                    is ResponseState.Error -> {
                        loader?.dismiss()
                        //serviceNavigation(naviGationValue,"slug")
                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        viewModel?.checkmerchantResponseLiveData?.value=null
                    }
                }
            }
            /*if(isFromUtilityPage){
                if (utilityValue.isNotEmpty()){
                    serviceNavigation(utilityValue)
                    utilityValue=""
                }
                isFromUtilityPage=false
            }*/
        }

        // 
        viewModel?.cashCollectionResponseLiveData?.observe(viewLifecycleOwner) {
        when (it) {
        is ResponseState.Loading -> {
            loader?.show()
        }
        is ResponseState.Success -> {
            loader?.dismiss()
           it.data?.redirecturl?.let {
               WebView(binding.root.context).set(it)

           }
            viewModel?.from_page_message?.value=null
            viewModel?.cashCollectionResponseLiveData?.value=null
/*
 "https://www.gibl.in/wallet/validate2/",
                   "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
 */
        }
        is ResponseState.Error -> {
            loader?.dismiss()
            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
            viewModel?.from_page_message?.value=null
            viewModel?.cashCollectionResponseLiveData?.value=null
        }
    }
}

    viewModel?.insuranceResponseLiveData?.observe(viewLifecycleOwner) {
        when (it) {
        is ResponseState.Loading -> {
            loader?.show()
        }
        is ResponseState.Success -> {
            loader?.dismiss()
           it.data?.data?.redirecturl?.let {redirecturl->
               it.data?.data?.retData?.let {retData->

                   //WebView(binding.root.context).set(redirecturl,retData)
                   /*var data="ret_data="+retData
                   WebView(binding.root.context).set(
                       redirecturl,
                       data
                   )*/
                   /*WebView(binding.root.context).set(
                       "https://www.gibl.in/wallet/validate2/",
                       "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
                   )*/
               }
           }
            viewModel?.from_page_message?.value=null
/*
 "https://www.gibl.in/wallet/validate2/",
                   "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
 */
            viewModel?.insuranceResponseLiveData?.value=null
        }
        is ResponseState.Error -> {
            loader?.dismiss()
            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
            viewModel?.from_page_message?.value=null
            viewModel?.insuranceResponseLiveData?.value=null
        }
    }
}



        viewModel?.electricStatelistResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                    activity?.let {act->

                        val stateListDialog = EStateListDialog(object : CallBack {
                            override fun getValue(s: String) {
                                viewModel?.state?.value=s

                                findNavController().navigate(R.id.action_homeFragment2_to_electricRechargeFragment)
                            }

                        },it.data?.stateList)
                        stateListDialog.show(act.supportFragmentManager, stateListDialog.tag)
                        viewModel?.electricStatelistResponseLiveData?.value=null
                    }
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel?.electricStatelistResponseLiveData?.value=null
                }
            }
        }

        viewModel?.viewRetailerModelResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    setRecycleView(it?.data?.data)

                    viewModel?.viewRetailerModelResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    //binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    viewModel?.viewRetailerModelResponseLiveData?.value=null
                }
            }
        }
        //****
        //temp2

    }

    private fun setRecycleView(data: ArrayList<ViewRetailerData>?) {
        activity?.let {act->

           /* retailerList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
            retailerList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
            retailerList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
*/
/*
@SerializedName("ID"           ) var ID          : String? = null
    @SerializedName("name"         ) var name        : String? = null
    @SerializedName("mobileNo"     ) var mobileNo    : String? = null
    @SerializedName("curr_balance" ) var currBalance : String? = null
    @SerializedName("bname"        ) var bname       : String? = null
 */
            val bankListBottomSheetDialog = RetailerListBottomSheetDialog(object : CallBack7 {
                override fun getValue7(ID: String, name: String, mobileNo: String, currBalance: String, bname: String,s1:String,s2:String) {
                    Constants.ID=ID
                    Constants.name=name
                    Constants.mobileNo=mobileNo
                    Constants.currBalance=currBalance
                    Constants.bname=bname

                    findNavController().navigate(R.id.action_homeFragment2_to_retailerPaymentFragment)
                    // Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                }
            },data)
            bankListBottomSheetDialog.show(
                act.supportFragmentManager,
                bankListBottomSheetDialog.tag
            )
        }
    }

    private fun getDeviceWIDTHandHeight() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        //var width = displayMetrics.widthPixels
        deviceHeight = displayMetrics.heightPixels

       // Log.d("TAG_deviceHeight", "getDeviceWIDTHandHeight: $deviceHeight")

    }


    fun checkService(navParameter: String,slag:String){
        naviGationValue=navParameter
        //Toast.makeText(requireContext(), ""+slag, Toast.LENGTH_SHORT).show()
        //serviceNavigation(naviGationValue,"slug")
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        if (isLogin){
            loginResponse?.let {loginData->
                viewModel?.apply {

                    val  data = mapOf(
                        "userid" to loginData.userid,
                        "service" to slag
                    )

                    val gson= Gson()
                    var jsonString = gson.toJson(data)
                    loginData.AuthToken?.let {
                        ServiceCheck(it,jsonString.encrypt())
                    }
                }

            }
        }
    }
    private fun serviceNavigation(s: String,slag:String) {

        if (isReport){
            viewModel.reportType.value = s//.replaceFirstChar(Char::titlecase)
            isReport=false
            findNavController().navigate(R.id.action_homeFragment2_to_reportFragment)

        }
        else {
            when (s) {
                //recycleViewEpayBanking
                getString(com.big9.app.R.string.scan) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_QRCodeFragment)
                }

                getString(R.string.ePotly) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_epotlyFragment)
                }

                getString(R.string.payment_request) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_paymentRequestFragment)
                }

                getString(R.string.move_to_wallet) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_moveToWalletFragment)
                }

                getString(R.string.move_to_bank) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_moveToBankFragment)
                }


                //recycleAccount

                getString(R.string.myaccount) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_userDetailsFragment)
                }

                getString(R.string.support) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_supportFragment)
                }

                getString(R.string.likeus) -> {}
                getString(R.string.usage_terms) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_termsAndConditionFragment)
                }

                getString(R.string.password) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_changePasswordFragment)
                }

                getString(R.string.certificate) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_certificateFragment)
                }


                //recycleViewEpayBanking

                getString(R.string.balance) -> {
                    aepsType="balance"
                    callCheckService()
                    //  showBalencePopup(binding.root.context)
                   /* activity?.let { act ->
                        val aadharAuthBottomSheetDialog =
                            AadharAuthBottomSheetDialog(object : CallBack {
                                override fun getValue(s: String) {
                                    findNavController().navigate(R.id.action_homeFragment2_to_balenceAEPSFragment)
                                }
                            })
                        aadharAuthBottomSheetDialog.show(
                            act.supportFragmentManager,
                            aadharAuthBottomSheetDialog.tag
                        )
                    }*/


                }

                getString(R.string.cash_withdraw) -> {
                    //isCashWithdraw = false
                    aepsType="withdraw"
                    callCheckService()
                    //findNavController().navigate(R.id.action_homeFragment2_to_cashWithdrawFragment)
                    /*activity?.let { act ->
                        val aadharAuthBottomSheetDialog =
                            AadharAuthBottomSheetDialog(object : CallBack {
                                override fun getValue(s: String) {
                                    findNavController().navigate(R.id.action_homeFragment2_to_cashWithdrawFragment)
                                }
                            })
                        aadharAuthBottomSheetDialog.show(
                            act.supportFragmentManager,
                            aadharAuthBottomSheetDialog.tag
                        )
                    }*/

                }


                getString(R.string.mini_statement) -> {
                    viewModel.reportType.value = getString(R.string.dmt)
                    activity?.let { act ->
                        aepsType="statement"
                        callCheckService()
                        //findNavController().navigate(R.id.action_homeFragment2_to_miniStatementFormFragment)
                        /*val aadharAuthBottomSheetDialog =
                            AadharAuthBottomSheetDialog(object : CallBack {
                                override fun getValue(s: String) {

                                    //findNavController().navigate(R.id.action_homeFragment2_to_miniStatementFragment)
                                    // findNavController().navigate(R.id.action_homeFragment2_to_cashWithdrawFragment)

                                }
                            })
                        aadharAuthBottomSheetDialog.show(
                            act.supportFragmentManager,
                            aadharAuthBottomSheetDialog.tag
                        )*/
                    }
                }

                getString(R.string.aadhar_pay) -> {
                    aepsType="aadhar_pay"
                    callCheckService()



                    //isCashWithdraw = true
                    //findNavController().navigate(R.id.action_homeFragment2_to_cashWithdrawFragment)
                    /*activity?.let { act ->
                        val aadharAuthBottomSheetDialog =
                            AadharAuthBottomSheetDialog(object : CallBack {
                                override fun getValue(s: String) {

                                }
                            })
                        aadharAuthBottomSheetDialog.show(
                            act.supportFragmentManager,
                            aadharAuthBottomSheetDialog.tag
                        )
                    }*/



                }


                //recycleEssential
                getString(R.string.prepaid) -> {
                    viewModel.prepaitOrPostPaid.value = Prepaid
                    viewModel.mobile.value = ""
                    viewModel.operator.value = ""
                    viewModel.amt.value = ""
                    findNavController().navigate(R.id.action_homeFragment2_to_mobileRechargeFragment)

                }

                getString(R.string.postpaid) -> {
                    viewModel.prepaitOrPostPaid.value = Postpaid
                    viewModel.mobile.value = ""
                    viewModel.operator.value = ""
                    viewModel.amt.value = ""
                    findNavController().navigate(R.id.action_homeFragment2_to_mobileRechargeFragment)
                }

                getString(R.string.dth_recharge) -> {
                    viewModel.subId.value = ""
                    viewModel.dthOperator.value = ""
                    viewModel.dthAmt.value = ""
                    findNavController().navigate(R.id.action_homeFragment2_to_DTHRechargeFragment)
                }

                getString(R.string.insurance) -> {
                    /*WebView(binding.root.context).set(
                        "https://www.gibl.in/wallet/validate2/",
                        "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
                    )*/
                    Toast.makeText(requireContext(), "Service unavailable. Coming soon.", Toast.LENGTH_SHORT).show()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            viewModel?.apply {

                                val  data = mapOf(
                                    "userid" to loginData.userid,
                                    "service" to slag
                                )

                                val gson= Gson()
                                var jsonString = gson.toJson(data)

                                loginData.AuthToken?.let {
                                 //   insurance(it,jsonString.encrypt(),getString(R.string.insurance))
                                }
                            }

                        }
                    }
                }


                //recycleUtility

                getString(R.string.electric) -> {
                    electricStateList()
                    /*activity?.let { act ->
                        val stateListDialog = StateListDialog(object : CallBack {
                            override fun getValue(s: String) {
                                viewModel?.state?.value = s
                                findNavController().navigate(R.id.action_homeFragment2_to_electricRechargeFragment)
                            }

                        }, stateList)
                        stateListDialog.show(act.supportFragmentManager, stateListDialog.tag)

                    }*/
                }

                getString(R.string.gas) -> {
                   /* activity?.let { act ->
                        val stateListDialog = StateListDialog(object : CallBack {
                            override fun getValue(s: String) {
                                viewModel?.state?.value = s
                                val gasBillerListDialog = GasBillerListDialog(object : CallBack {
                                    override fun getValue(s: String) {
                                        viewModel?.gasBiller?.value = s
                                        findNavController().navigate(R.id.action_homeFragment2_to_gasBookingFragment)
                                    }

                                })
                                gasBillerListDialog.show(
                                    act.supportFragmentManager,
                                    gasBillerListDialog.tag
                                )

                            }

                        }, stateList)
                        stateListDialog.show(act.supportFragmentManager, stateListDialog.tag)

                    }*/
                }


                getString(R.string.view_more) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_viewMoreFragment)
                }

                //recycleFinancial


                getString(R.string.prepaid) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_mobileRechargeFragment)
                }

                getString(R.string.postpaid) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_mobileRechargeFragment)
                }

                getString(R.string.dth_recharge) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_DTHRechargeFragment)
                }

                getString(R.string.money_transfer) -> {
                    viewModel?.nameSendMoneyErrorVisible?.value=false
                    findNavController().navigate(R.id.action_homeFragment2_to_moneyTranspherFragment)
                }

                getString(R.string.credit_card) -> {
                    viewModel?.apply {
                        credit_card.value=""
                        credit_holder_name.value=""
                        credit_mobile.value=""
                        credit_mobile.value=""
                        credit_amt.value=""
                        credit_remarks.value=""
                        otp.value=""
                    }
                    findNavController().navigate(R.id.action_homeFragment2_to_creditCardPaymentFragment)
                }

                getString(R.string.cash_collection) -> {


                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            viewModel?.apply {

                                val  data = mapOf(
                                    "userid" to loginData.userid,
                                    "service" to slag
                                )

                                val gson= Gson()
                                var jsonString = gson.toJson(data)

                                loginData.AuthToken?.let {
                                    cashCollection(it,jsonString.encrypt(),getString(R.string.cash_collection))
                                }
                            }

                        }
                    }

                    /*val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    loginResponse?.let { loginData ->
                        loginData.userid?.let {
                            val data = mapOf("userid" to it,
                                )
                            val gson = Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                Log.d("TAG_sss", "serviceNavigation: "+jsonString.encrypt())
                                viewModel?.cashCollection(it,jsonString.encrypt())//"jsonString.encrypt()"
                            }
                        }
                    }*/

                    /*WebView(binding.root.context).set(
                        "https://www.gibl.in/wallet/validate2/",
                        "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
                    )*/
                    //

                    /*val (isLogin, loginResponse) = sharedPreff.getLoginData()
                    loginResponse?.let { loginData ->
                        loginData.userid?.let {
                            val data = mapOf(
                                "userid" to loginData.userid

                            )
                            val gson = Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                viewModel?.cashCollection(it, jsonString.encrypt())
                            }
                        }
}*/
                }



                getString(R.string.matm) -> {
                    activity?.let { act ->
                        checkPermissionForBlooth()

                        val microATMBottomSheetDialog =
                            MicroATMBottomSheetDialog(object : CallBack {
                                override fun getValue(microAtmTitle: String) {


                                    val selectTransactionTypeBottomSheetDialog =
                                        SelectTransactionTypeBottomSheetDialog(object :
                                            CallBack {
                                            override fun getValue(s: String) {

                                                val tpinBottomSheetDialog =
                                                    TpinBottomSheetDialog(object :
                                                        CallBack {
                                                        override fun getValue(s: String) {
                                                            when (microAtmTitle) {
                                                                "balence_enquery" -> {
                                                                    Toast.makeText(
                                                                        requireActivity(),
                                                                        "$s " + "balence_enquery",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }

                                                                "balence_withdraw" -> {
                                                                    Toast.makeText(
                                                                        requireActivity(),
                                                                        "$s " + "balence_withdraw",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                            }

                                                        }
                                                    })
                                                tpinBottomSheetDialog.show(
                                                    act.supportFragmentManager,
                                                    tpinBottomSheetDialog.tag
                                                )


                                            }
                                        })

                                    selectTransactionTypeBottomSheetDialog.show(
                                        act.supportFragmentManager,
                                        selectTransactionTypeBottomSheetDialog.tag
                                    )

                                }
                            })
                        microATMBottomSheetDialog.show(
                            act.supportFragmentManager,
                            microATMBottomSheetDialog.tag
                        )
                    }
                }


                getString(R.string.electric) -> {
                    electricStateList()
                    /*activity?.let { act ->
                        val stateListDialog = StateListDialog(object : CallBack {
                            override fun getValue(s: String) {
                                viewModel?.state?.value = s
                                findNavController().navigate(R.id.action_homeFragment2_to_electricRechargeFragment)
                            }

                        })
                        stateListDialog.show(act.supportFragmentManager, stateListDialog.tag)

                    }*/
                }

                //recycleUtility
                getString(R.string.fast_tag) -> {
                    findNavController().navigate(R.id.action_homeFragment2_to_fastTagFragment)
                }

            }

        }
    }

    private fun callCheckService() {
        val (isLogin2, loginResponse2) =sharedPreff.getLoginData()
        if (isLogin2){
            loginResponse2?.let {loginData->
                viewModel?.apply {

                    val  data = mapOf(
                        "userid" to loginData.userid,
                        "servicename" to aepsType
                    )

                    val gson= Gson()
                    var jsonString = gson.toJson(data)

                    loginData.AuthToken?.let {
                        checkmerchant(it,jsonString.encrypt())
                    }
                }

            }
        }
    }


    private fun checkPermissionForBlooth() {
        /*for (permission in permissionList) {
            if (ActivityCompat.checkSelfPermission(binding.root.context, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsForBloothRequest.add(permission)
            }
        }*/
        //enableBlueTooth()
        Dexter.withContext(binding.root.context)
            .withPermissions(permissionsForBloothRequest)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // Check if user has granted all
                    if (report?.areAllPermissionsGranted() == true) {
                        enableBlueTooth()
                    } else {
                        Toast.makeText(binding.root.context, "Permission deny", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    // User has denied a permission, proceed and ask them again
                    token?.continuePermissionRequest()
                }
            }).check()

    }

    private fun enableBlueTooth() {

        if (bluetoothAdapter?.isEnabled == false) {
            //bluetoothAdapter?.enable()
            val intent = (Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            if (ActivityCompat.checkSelfPermission(
                    binding.root.context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermissionForBlooth()
                return
            }
            startActivity(intent)

        }
        var device=bluetoothManager.adapter.bondedDevices
        var deviceAddress=""
        device.forEach{
            Log.d("TAG_device", "enableBlueTooth: name:"+it.name+" address:"+it.address)
            deviceAddress=it.address
        }

        bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)

        // Pair with the device
       // pairWithBluetoothDevice(bluetoothDevice)

        // Connect and transfer data
        activity?.let {act->
            checkConnectAndTransferData(act)
        }


    }
    private fun pairWithBluetoothDevice(device: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (!device.createBond()) {
                Toast.makeText(binding.root.context, "connect  pair with the device", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun checkConnectAndTransferData(act: FragmentActivity) {
        // Check and request Bluetooth permissions if needed
        if (ActivityCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                act,
                arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_BLUETOOTH_PERMISSION
            )
        } else {
            // Permissions granted, proceed with Bluetooth operations
            connectAndTransferData()
        }
        /*try {
            if (ActivityCompat.checkSelfPermission(
                    binding.root.context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.randomUUID())
            bluetoothSocket.connect()
            outputStream = bluetoothSocket.outputStream

            // Send data
            val dataToSend = "Hello, Bluetooth Device!"
            outputStream.write(dataToSend.toByteArray())

            // Close the socket and output stream when done
            outputStream.close()
            bluetoothSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle connection or data transfer errors
        }*/
    }

    private fun connectAndTransferData() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    binding.root.context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.randomUUID())
            bluetoothSocket.connect()
            outputStream = bluetoothSocket.outputStream
            inputStream = bluetoothSocket.inputStream

            // Send data
            val dataToSend = "Hello, Bluetooth Device!"
            outputStream.write(dataToSend.toByteArray())

            // Read response
            val buffer = ByteArray(1024)
            val bytesRead = inputStream.read(buffer)
            if (bytesRead != -1) {
                val receivedData = String(buffer, 0, bytesRead)
                // Handle the received data
                Toast.makeText(binding.root.context, "Received data: $receivedData", Toast.LENGTH_SHORT).show()

            }

            // Close the streams and socket when done
            outputStream.close()
            inputStream.close()
            bluetoothSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(binding.root.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()

            // Handle connection or data transfer errors
        }
    }


    private fun viewOnClick() {
        binding.apply {
            imgBalance.setOnClickListener{
                sharedPreff?.getUserData()?.let{
                    showBalencePopup(binding.root.context,it.currBalance?.formatAsIndianCurrency(),it.payoutBalance?.formatAsIndianCurrency(),it.lienbal?.formatAsIndianCurrency(),it.lienbalPayout?.formatAsIndianCurrency())
                }
            }
            imgSearch.setOnClickListener{
                        searchList.clear()
                        //searchList.addAll(iconList)
                        //searchList.addAll(iconList2)
                        searchList.addAll(iconList3)
                        //searchList.addAll(iconList4)
                        searchList.addAll(iconList5)
                        searchList.addAll(iconList6)
                        searchList.addAll(iconList7)
                        //searchList.addAll(iconList8)
                        searchList.addAll(iconList9)
                        searchList.addAll(iconList10)
                        searchList.addAll(iconList11)
                        searchList.addAll(iconList12)
                findNavController().navigate(R.id.action_homeFragment2_to_searchFragment)
            }

            rotateView(arrowImageView, 0f)
            val collapseAnimation: Animation =
                AnimationUtils.loadAnimation(requireActivity(), R.anim.collapse_animation)
            arrowImageView.setOnClickListener {

                if (isRotated) {

                    rotateView(arrowImageView, 0f)


                    val layoutParams = llContainer.layoutParams
                    layoutParams.height = 40
                    llContainer.layoutParams = layoutParams
                    llContainer.startAnimation(collapseAnimation)

                    llContainer.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageView, 180f)
                    val layoutParams = llContainer.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    llContainer.layoutParams = layoutParams
                    llContainer.visibility = View.VISIBLE
                    llContainer.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotated = !isRotated
            }



        /*    arrowImageView2.setOnClickListener {

                if (isRotated2) {

                    rotateView(arrowImageView2, 0f)


                    val layoutParams = linearLayout.layoutParams
                    layoutParams.height = 40
                    linearLayout.layoutParams = layoutParams
                    linearLayout.startAnimation(collapseAnimation)

                    linearLayout.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageView2, 180f)
                    val layoutParams = linearLayout.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    linearLayout.layoutParams = layoutParams
                    linearLayout.visibility = View.VISIBLE
                    linearLayout.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotated2 = !isRotated2
            }
*/



            arrowImageViewNew2Aeps.setOnClickListener {

                if (isRotated2) {

                    rotateView(arrowImageViewNew2Aeps, 0f)


                    val layoutParams = recycleAEPS.layoutParams
                    layoutParams.height = 40
                    recycleAEPS.layoutParams = layoutParams
                    recycleAEPS.startAnimation(collapseAnimation)

                    recycleAEPS.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageViewNew2Aeps, 180f)
                    val layoutParams = recycleAEPS.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    recycleAEPS.layoutParams = layoutParams
                    recycleAEPS.visibility = View.VISIBLE
                    recycleAEPS.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotated2 = !isRotated2
            }


            arrowImageViewNewEssential.setOnClickListener {

                if (isRotated3) {

                    rotateView(arrowImageViewNewEssential, 0f)


                    val layoutParams = recycleEssential.layoutParams
                    layoutParams.height = 40
                    recycleEssential.layoutParams = layoutParams
                    recycleEssential.startAnimation(collapseAnimation)

                    recycleEssential.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageViewNewEssential, 180f)
                    val layoutParams = recycleEssential.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    recycleEssential.layoutParams = layoutParams
                    recycleEssential.visibility = View.VISIBLE
                    recycleEssential.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotated3 = !isRotated3
            }

            arrowImageViewNewMostUses.setOnClickListener {

                if (isRotated3) {

                    rotateView(arrowImageViewNewMostUses, 0f)


                    val layoutParams = recycleMostUses.layoutParams
                    layoutParams.height = 40
                    recycleMostUses.layoutParams = layoutParams
                    recycleMostUses.startAnimation(collapseAnimation)

                    recycleMostUses.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageViewNewMostUses, 180f)
                    val layoutParams = recycleMostUses.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    recycleMostUses.layoutParams = layoutParams
                    recycleMostUses.visibility = View.VISIBLE
                    recycleMostUses.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotated3 = !isRotated3
            }
            /*arrowImageViewNew3.setOnClickListener {

                if (isRotated3) {

                    rotateView(arrowImageViewNew3, 0f)


                    val layoutParams = linearLayoutNew3.layoutParams
                    layoutParams.height = 40
                    linearLayoutNew3.layoutParams = layoutParams
                    linearLayoutNew3.startAnimation(collapseAnimation)

                    linearLayoutNew3.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageViewNew3, 180f)
                    val layoutParams = linearLayoutNew3.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    linearLayoutNew3.layoutParams = layoutParams
                    linearLayoutNew3.visibility = View.VISIBLE
                    linearLayoutNew3.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotated3 = !isRotated3
            }*/


            arrowImageViewNew4.setOnClickListener {

                if (isRotated4) {

                    rotateView(arrowImageViewNew4, 0f)


                    val layoutParams = linearLayoutNew4.layoutParams
                    layoutParams.height = 40
                    linearLayoutNew4.layoutParams = layoutParams
                    linearLayoutNew4.startAnimation(collapseAnimation)

                    linearLayoutNew4.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageViewNew4, 180f)
                    val layoutParams = linearLayoutNew4.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    linearLayoutNew4.layoutParams = layoutParams
                    linearLayoutNew4.visibility = View.VISIBLE
                    linearLayoutNew4.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotated4 = !isRotated4
            }

            arrowImageViewNew44.setOnClickListener {

                if (isRotated5) {

                    rotateView(arrowImageViewNew44, 0f)


                    val layoutParams = recycleFinancial.layoutParams
                    layoutParams.height = 40
                    recycleFinancial.layoutParams = layoutParams
                    recycleFinancial.startAnimation(collapseAnimation)

                    recycleFinancial.visibility = View.INVISIBLE

                } else {
                    rotateView(arrowImageViewNew4, 180f)
                    val layoutParams = recycleFinancial.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    recycleFinancial.layoutParams = layoutParams
                    recycleFinancial.visibility = View.VISIBLE
                    recycleFinancial.startAnimation(
                        AnimationUtils.loadAnimation(
                            requireActivity(),
                            R.anim.expand_animation
                        )
                    )

                }

                isRotated5 = !isRotated5
            }




            recycleEssential.apply {
                iconList3.clear()
                iconList3.add(ListIcon(getString(R.string.prepaid), R.drawable.db_mobile,getString(R.string.mobile_slag)))
                iconList3.add(ListIcon(getString(R.string.postpaid), R.drawable.db_mobile,getString(R.string.mobile_slag)))
                iconList3.add(ListIcon(getString(R.string.dth_recharge), R.drawable.ic_dth_recharge,getString(R.string.dth_recharge_slag)))
                /*iconList3.add(ListIcon(getString(R.string.electric), R.drawable.electric))
                iconList3.add(ListIcon("Fast Tag", R.drawable.icons8_fastag))
                iconList3.add(ListIcon("Google Play", R.drawable.google_play))*/
                iconList3.add(ListIcon(getString(R.string.insurance), R.drawable.insurance,getString(R.string.insurance_slag)))
                /*iconList3.add(ListIcon("Water", R.drawable.water))
                iconList3.add(ListIcon("View More", R.drawable.view_more))*/
                adapter= EssentialAdapter(iconList3,R.drawable.circle_shape2, object : CallBack2 {
                    override fun getValue2(s: String,slag: String) {
                        if(s==getString(R.string.insurance) ){
                            Toast.makeText(requireContext(), "Service unavailable. Coming soon.", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            checkService(s,slag)
                        }
                        //checkService(s,slag)
                        //serviceNavigation(s)
                        /*when(s){
                            getString(R.string.prepaid)->{
                                viewModel.prepaitOrPostPaid.value="Prepaid"
                                findNavController().navigate(R.id.action_homeFragment2_to_mobileRechargeFragment)
                            }
                            getString(R.string.postpaid)->{
                                viewModel.prepaitOrPostPaid.value="Postpaid"
                                findNavController().navigate(R.id.action_homeFragment2_to_mobileRechargeFragment)
                            }

                            getString(R.string.dth_recharge)->{
                                findNavController().navigate(R.id.action_homeFragment2_to_DTHRechargeFragment)
                            }




                        }*/
                    }

                })
            }




            recycleFinancial.apply {
                iconList11.clear()
                iconList11.add(ListIcon(getString(R.string.credit_card), R.drawable.credit_card,getString(R.string.credit_card_slag)))
                iconList11.add(ListIcon(getString(R.string.cash_collection), R.drawable.cash_collection,getString(R.string.cash_collection_slag)))
                iconList11.add(ListIcon(getString(R.string.matm), R.drawable.matm,getString(R.string.matm_slag)))
                iconList11.add(ListIcon(getString(R.string.money_transfer), R.drawable.imps,getString(R.string.imps_slag)))
                /*iconList3.add(ListIcon("Water", R.drawable.water))
                iconList3.add(ListIcon("View More", R.drawable.view_more))*/
                adapter= FinancialAdapter(iconList11,R.drawable.circle_shape2, object : CallBack2 {
                    override fun getValue2(s: String,tag: String) {

                        if(s==getString(R.string.matm) ){
                            Toast.makeText(requireContext(), "Service unavailable. Coming soon.", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            checkService(s,tag)
                        }


                        //need comment. now testing perpose
                        //serviceNavigation(s,tag)
                        /*when(s){
                            getString(R.string.prepaid)->{
                                findNavController().navigate(R.id.action_homeFragment2_to_mobileRechargeFragment)
                            }
                            getString(R.string.postpaid)->{
                                findNavController().navigate(R.id.action_homeFragment2_to_mobileRechargeFragment)
                            }

                            getString(R.string.dth_recharge)->{
                                findNavController().navigate(R.id.action_homeFragment2_to_DTHRechargeFragment)
                            }
                            getString(R.string.money_transfer)->{
                                findNavController().navigate(R.id.action_homeFragment2_to_moneyTranspherFragment)
                            }
                            getString(R.string.credit_card)->{
                                findNavController().navigate(R.id.action_homeFragment2_to_creditCardPaymentFragment)
                            }



                            getString(R.string.electric)->{
                                activity?.let {act->
                                    val stateListDialog = StateListDialog(object : CallBack {
                                        override fun getValue(s: String) {
                                            viewModel?.state?.value=s
                                            findNavController().navigate(R.id.action_homeFragment2_to_electricRechargeFragment)
                                        }

                                    })
                                    stateListDialog.show(act.supportFragmentManager, stateListDialog.tag)

                                }
                            }


                        }*/
                    }

                })
            }


            recycleUtility.apply {
                iconList10.clear()
                iconList10.add(ListIcon(getString(R.string.electric), R.drawable.electric,getString(R.string.bill_pay)))
                iconList10.add(ListIcon(getString(R.string.gas), R.drawable.gas_ioc,getString(R.string.bill_pay)))
                iconList10.add(ListIcon(getString(R.string.fast_tag), R.drawable.icons8_fastag,getString(R.string.fast_tag_slag)))
                iconList10.add(ListIcon(getString(R.string.view_more), R.drawable.view_more,getString(R.string.view_more)))
                adapter= UtilityAdapter(iconList10,R.drawable.circle_shape2, object : CallBack2 {
                    override fun getValue2(s: String,slag: String) {
                        if(s== getString(R.string.view_more)){
                            findNavController().navigate(R.id.action_homeFragment2_to_viewMoreFragment)
                        }
                        else{
                            if(s==getString(R.string.gas) || s==getString(R.string.fast_tag) || s==getString(R.string.fast_tag)){
                                Toast.makeText(requireContext(), "Service unavailable. Coming soon.", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                checkService(s,slag)
                            }
                            //checkService(s,slag)
                        }

                        //serviceNavigation(s)
                        /*when(s){

                            getString(R.string.electric)->{
                                activity?.let {act->
                                    val stateListDialog = StateListDialog(object : CallBack {
                                        override fun getValue(s: String) {
                                            viewModel?.state?.value=s
                                            findNavController().navigate(R.id.action_homeFragment2_to_electricRechargeFragment)
                                        }

                                    })
                                    stateListDialog.show(act.supportFragmentManager, stateListDialog.tag)

                                }
                            }
                            getString(R.string.view_more)->{}

                        }*/
                    }

                })
            }




            recycleMostUses.apply {
              //  requestFocus()
                iconList12.clear()
                iconList12.add(ListIcon(getString(R.string.electric), R.drawable.electric,getString(R.string.bill_pay)))

                adapter= MostPopularAdapter(iconList12,R.drawable.circle_shape2, object : CallBack {
                    override fun getValue(s: String) {
                        when(s){

                            getString(R.string.electric)->{
                                electricStateList()

                            }
                            getString(R.string.view_more)->{}

                        }
                    }

                })
            }









           /* recycleMyBig.apply {
                iconList4.clear()
                iconList4.add(ListIcon(getString(R.string.balance), R.drawable.transaction_history))
                iconList4.add(ListIcon("CMS", R.drawable.cms))
                iconList4.add(ListIcon("Wallet", R.drawable.db_balance))
                iconList4.add(ListIcon("Postpaid", R.drawable.cms))

                adapter= MyBig9Adapter(iconList4,R.drawable.circle_shape2,object : CallBack{
                    override fun getValue(s: String) {
                        when(s){
                            getString(R.string.balance)->{
                                showBalencePopup(binding.root.context)
                            }
                        }

                    }

                })
            }*/

            recycleAEPS.apply {
                iconList5.clear()
                iconList5.add(ListIcon(getString(R.string.balance), R.drawable.transaction_history,getString(R.string.balance_slag)))
                iconList5.add(ListIcon(getString(R.string.cash_withdraw), R.drawable.cashcol,getString(R.string.cash_withdraw_slag)))
                iconList5.add(ListIcon(getString(R.string.mini_statement), R.drawable.ministatement,getString(R.string.mini_statement_slag)))

                iconList5.add(ListIcon(getString(R.string.aadhar_pay), R.drawable.aadharpay,getString(R.string.aadhar_pay_slag)))

                adapter= AEPSAdapter(iconList5,R.drawable.circle_shape2,object : CallBack2 {
                    override fun getValue2(s: String,tag: String) {
                        Toast.makeText(requireContext(), "Service unavailable. Coming soon.", Toast.LENGTH_SHORT).show()

                        //******

                        serviceNavigation(s,tag)
                        //checkService(s,tag)
                       //-m  serviceNavigation(s,tag)
                       // serviceNavigation(s)
                       /* when(s) {
                            getString(R.string.balance) -> {
                                showBalencePopup(binding.root.context)
                            }

                            getString(R.string.cash_withdraw) -> {
                                isCashWithdraw=true
                                findNavController().navigate(R.id.action_homeFragment2_to_cashWithdrawFragment)
                            }


                            getString(R.string.mini_statement) -> {
                                viewModel.reportType.value=getString(R.string.dmt)
                                findNavController().navigate(R.id.action_homeFragment2_to_miniStatementFragment)
                               // findNavController().navigate(R.id.action_homeFragment2_to_cashWithdrawFragment)
                            }

                            getString(R.string.aadhar_pay) -> {
                                isCashWithdraw=false
                                findNavController().navigate(R.id.action_homeFragment2_to_cashWithdrawFragment)
                            }




                        }*/
                    }
                })

            }

            recycleViewEpayBanking.apply {
                iconList6.clear()
                iconList6.add(ListIcon(getString(R.string.move_to_bank), R.drawable.bank_transfer_icon,getString(R.string.move_to_bank_slag)))
                iconList6.add(ListIcon(getString(R.string.move_to_wallet), R.drawable.balance,getString(R.string.move_to_wallet_slag)))
                iconList6.add(ListIcon(getString(R.string.ePotly), R.drawable.epotlyinb,getString(R.string.ePotly_slag)))
                iconList6.add(ListIcon(getString(R.string.payment_request), R.drawable.balance,getString(R.string.payment_request_slag)))
                //circle_shape
                adapter= BankingAdapter(iconList6,R.drawable.circle_shape2, object : CallBack2 {
                    override fun getValue2(s: String,slag: String) {
                        serviceNavigation(s,slag)
                        if(s==getString(R.string.move_to_bank) || s==getString(R.string.move_to_wallet)){
                            Toast.makeText(requireContext(), "Service unavailable. Coming soon.", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            checkService(s,slag)
                        }


                       // serviceNavigation(s)
                    }

                })
            }

            recycleRetailerViewEpayBanking.apply {
                iconList13.clear()
                iconList13.add(ListIcon(getString(R.string.add_retailers) , R.drawable.add_retailer,""))
                iconList13.add(ListIcon("View Channel" , R.drawable.view_retailer,""))
               /* iconList6.add(ListIcon(getString(R.string.move_to_wallet), R.drawable.balance,getString(R.string.move_to_wallet_slag)))
                iconList6.add(ListIcon(getString(R.string.ePotly), R.drawable.epotlyinb,getString(R.string.ePotly_slag)))
                iconList6.add(ListIcon(getString(R.string.payment_request), R.drawable.balance,getString(R.string.payment_request_slag)))*/
                //circle_shape
                adapter= RetailerAdapter(iconList13,R.drawable.circle_shape2, object : CallBack2 {
                    override fun getValue2(s: String,slag: String) {
                        //checkService(s,slag)
                        //serviceNavigation(s)
                        when(s){
                            getString(R.string.add_retailers) -> {
                                viewModel?.ratailerMobile?.value=""
                                findNavController().navigate(R.id.action_homeFragment2_to_addRetailerFragment)
                            }

                            getString(R.string.view_channel) -> {
                                val (isLogin, loginResponse) = sharedPreff.getLoginData()
                                loginResponse?.let { loginData ->
                                    loginData.userid?.let {

                                        val data = mapOf(
                                            "userid" to loginData.userid
                                        )
                                        val gson = Gson()
                                        var jsonString = gson.toJson(data)
                                        loginData.AuthToken?.let {
                                            viewModel?.ViewRetailerModel(it, jsonString.encrypt())
                                        }
                                    }
                                }

                            }
                        }

                    }

                })
            }


            recycleTravel.apply {
                iconList7.clear()
                iconList7.add(ListIcon(getString(R.string.flight), R.drawable.ic_flight,getString(R.string.flight_slag)))
                iconList7.add(ListIcon(getString(R.string.train), R.drawable.ic_train,"No Slag"))
                iconList7.add(ListIcon(getString(R.string.bus), R.drawable.bus,"No Slag"))
                iconList7.add(ListIcon(getString(R.string.hotel), R.drawable.hotel,"No Slag"))

                adapter= TravelAdapter(iconList7, R.drawable.circle_shape2, object : CallBack2 {
                    override fun getValue2(s: String,slag: String) {
                            Toast.makeText(requireContext(), "Service unavailable. Coming soon.", Toast.LENGTH_SHORT).show()

                        //checkService(s,slag)
                        //serviceNavigation(s)
                    }

                })
            }


            recycleReport.apply {
                iconList8.clear()
                iconList8.add(ListIcon(getString(R.string.payment), R.drawable.report,getString(R.string.payment_slag)))
                iconList8.add(ListIcon(getString(R.string.transactions), R.drawable.report,getString(R.string.transactions_slag)))
                iconList8.add(ListIcon(getString(R.string.dmt), R.drawable.report,getString(R.string.dmt_slag)))
                iconList8.add(ListIcon(getString(R.string.load_Requests), R.drawable.report,getString(R.string.load_Requests_slag)))
                iconList8.add(ListIcon(getString(R.string.wallet_ledger), R.drawable.report,getString(R.string.wallet_ledger_slag)))
                iconList8.add(ListIcon(getString(R.string.cashout_ledger), R.drawable.report,"No slag"))
                iconList8.add(ListIcon(getString(R.string.aeps), R.drawable.report,getString(R.string.aeps_slag)))
                iconList8.add(ListIcon(getString(R.string.micro_atm), R.drawable.report,getString(R.string.micro_atm_slag)))
                iconList8.add(ListIcon(getString(R.string.commissions), R.drawable.report,getString(R.string.commissions_slag)))
                iconList8.add(ListIcon(getString(R.string.bank_settle), R.drawable.report,getString(R.string.bank_settle_slag)))
                iconList8.add(ListIcon(getString(R.string.wallet_settle), R.drawable.report,getString(R.string.wallet_settle_slag)))
                iconList8.add(ListIcon(getString(R.string.complaints), R.drawable.report,getString(R.string.complaints_slag)))


                adapter= ReportAdapter(iconList8,R.drawable.circle_shape2,object : CallBack2 {
                    override fun getValue2(s: String,tag: String) {
                        tableViewModel.deleteAllData()
                         reportList.clear()
                         reportList2.clear()


                        reportAdapter?.let {
                            reportList.clear()
                            newReportList.clear()
                        it.items=ArrayList()
                        it.notifyDataSetChanged()
                        }
                        commissionReportAdapter?.let {
                            commissionReportList.clear()
                            commissionReportList2.clear()
                            commissionReportAdapter?.items= ArrayList()
                            commissionReportAdapter?.notifyDataSetChanged()
                        }
                        isReport=true
                        viewModel?.reportType?.value = s
                       // checkService(s,tag)
                        serviceNavigation(s,tag)

                        //Toast.makeText(requireContext(), "Service unavailable. Coming soon.", Toast.LENGTH_SHORT).show()

                        /*if (s==getString(R.string.commissions)){

                            findNavController().navigate(R.id.action_homeFragment2_to_commissionReportFragment)
                        }*/
                       /* if (s==getString(R.string.wallet_ledger)){
                            commissionReportAdapter?.let {
                                commissionReportList.clear()
                                commissionReportList2.clear()
                                commissionReportAdapter?.items= ArrayList()
                                commissionReportAdapter?.notifyDataSetChanged()
                            }
                            findNavController().navigate(R.id.action_homeFragment2_to_walletLedgerReportFragment)
                        }*/

                        //else {
                            //viewModel.reportType.value = s//.replaceFirstChar(Char::titlecase)
                           // findNavController().navigate(R.id.action_homeFragment2_to_reportFragment)
                        //}
                       /*when(s){

                           getString(R.string.payment)->{}
                           getString(R.string.transactions)->{}
                           getString(R.string.dmt)->{}
                           getString(R.string.load_Requests)->{}
                           getString(R.string.wallet_ledger)->{}
                           getString(R.string.cashout_ledger)->{}
                           getString(R.string.aeps)->{}
                           getString(R.string.micro_atm)->{}
                           getString(R.string.commissions)->{}
                           getString(R.string.bank_settle)->{}
                           getString(R.string.wallet_settle)->{}
                           getString(R.string.complaints)->{}

                       }*/
                    }

                })
            }

            recycleAccount.apply {

                iconList9.clear()
                iconList9.add(ListIcon(getString(R.string.myaccount), R.drawable.myaccount,getString(R.string.myaccount_slag)))
                iconList9.add(ListIcon(getString(R.string.support), R.drawable.baseline_notifications_24,getString(R.string.support_slag)))
                iconList9.add(ListIcon(getString(R.string.likeus), R.drawable.like_us,getString(R.string.likeus_slag)))
                iconList9.add(ListIcon(getString(R.string.usage_terms), R.drawable.baseline_assignment_24,getString(R.string.usage_terms_slag)))

                iconList9.add(ListIcon(getString(R.string.password), R.drawable.baseline_lock_person_24,getString(R.string.password_slag)))
                iconList9.add(ListIcon(getString(R.string.certificate), R.drawable.baseline_receipt_24,getString(R.string.certificate_slag)))
                iconList9.add(ListIcon(getString(R.string.logout), R.drawable.baseline_logout_24,getString(R.string.logout)))


                adapter= ReportAdapter(iconList9,R.drawable.circle_shape2,object : CallBack2 {
                    override fun getValue2(s: String,tag: String) {
                       if(s== getString(R.string.logout)){
                            context?.let { ctx->
                                ctx.userLogout()
                            }
                        }
                        else{
                            //checkService(s,tag)
                           serviceNavigation(s,tag)
                        }

                       // serviceNavigation(s)
                       /* when(s){
                           getString(R.string.myaccount)->{
                            findNavController().navigate(R.id.action_homeFragment2_to_userDetailsFragment)
                           }
                           getString(R.string.support)->{
                               findNavController().navigate(R.id.action_homeFragment2_to_supportFragment)
                           }
                           getString(R.string.likeus)->{}
                           getString(R.string.usage_terms)->{
                               findNavController().navigate(R.id.action_homeFragment2_to_termsAndConditionFragment)
                           }
                           getString(R.string.password)->{
                               findNavController().navigate(R.id.action_homeFragment2_to_changePasswordFragment)
                           }
                           getString(R.string.certificate)->{
                               findNavController().navigate(R.id.action_homeFragment2_to_certificateFragment)
                           }
                           getString(R.string.logout)->{
                               context?.let { ctx->
                                   ctx.userLogout()
                               }
                           }
                        }*/
                    }

                })
            }

        }
    }



    /*private fun rotateView(view: View, degrees: Float) {
        val rotation = ObjectAnimator.ofFloat(view, "rotation", degrees)
        rotation.duration = 500 // Adjust the duration as needed
        rotation.start()
    }*/

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun init() {
        //Log.d("TAG_token", "init: "+sharedPreff.getFcnToken().toString())
        activity?.let {
            bluetoothManager = it.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothManager?.adapter?.let {
                bluetoothAdapter =it
            }
        }



        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            loginResponse?.let { loginData ->
                loginData.name?.let {
                    binding.tvUser.text=it
                }
                if(loginData.UserType=="D" || loginData.UserType=="SD"){
                    binding.cardRetailer.visibility=View.VISIBLE
                }
                else{
                    binding.cardRetailer.visibility=View.GONE
                }

            }


        }
        /**/
        viewModel.from_page_message.value="home"
        checkPermission()
        //sharedPreff.setTestData("Abcd")
        //Toast.makeText(requireActivity(), ""+sharedPreff.getTestData(), Toast.LENGTH_SHORT).show()
        iconList.add(ListIcon("Card", R.drawable.bb1,""))
        iconList.add(ListIcon("Card", R.drawable.b3,""))
        iconList.add(ListIcon("Card", R.drawable.card,""))
        iconList.add(ListIcon("Card", R.drawable.card2,""))
        iconList.add(ListIcon("Card", R.drawable.bb1,""))
        iconList.add(ListIcon("Card", R.drawable.card2,""))
        iconList.add(ListIcon("Card", R.drawable.card,""))
        iconList.add(ListIcon("Card", R.drawable.card2,""))
        binding.viewPager2.apply {
            val scaleMin = 0.32f // Minimum scale
            val scaleMax = 0.45f // Maximum scale
            setupCarousel(this, scaleMin, scaleMax)
            //offscreenPageLimit = 3
            /* setPageTransformer { page, position ->
                 page.translationX =
                     -position * (page.width / 3f)
             }*/
            adapter = BannerViewpagerAdapter(iconList)

            currentItem = 2


            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    // Check if the user scrolls to the first or last item, then perform looping.
                    if (position == 0) {
                        // Scroll to the last item.
                        // currentItem=adapter?.itemCount?.minus(1) ?: 0
                        // binding.viewPager2.setCurrentItem(adapter.getItemCount() - 1, false)
                    } else if (position == (adapter?.itemCount ?: 0)) {
                        // Scroll to the first item.
                        binding.viewPager2.setCurrentItem(0, false)
                    }
                }
            })

        }
        iconList2.add(ListIcon("Card", R.drawable.sa1,""))
        iconList2.add(ListIcon("Card", R.drawable.sa2,""))
        iconList2.add(ListIcon("Card", R.drawable.sa3,""))
        iconList2.add(ListIcon("Card", R.drawable.sa4,""))
        iconList2.add(ListIcon("Card", R.drawable.sa2,""))
        iconList2.add(ListIcon("Card", R.drawable.sa5,""))
        binding.viewPager3.apply {
            autoScrollHandler = AutoScrollHandler(this)
            adapter = BannerViewpagerAdapter(iconList2)
        }
        binding.tvMessage.isSelected = true

        binding.tvAppVersion.text="App version " +MethodClass.appVersion(binding.root.context)

    }

    fun setupCarousel(
        viewPager: ViewPager2,
        minScale: Float = 0.35f,
        nextItemAlpha: Float = 0.5f
    ) {
        viewPager.offscreenPageLimit = 1

        val nextItemVisiblePx =
            viewPager.context.resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            viewPager.context.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position

            //get x-y ratio
            val ratio = page.scaleX / page.scaleY
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (minScale * Math.abs(position))
            // Next line scales the item's width. You can remove it if you don't want this effect
            /*if (!(ratio * page.scaleY).isNaN())
                page.scaleX = ratio * page.scaleY
            else
                page.scaleX = 1 - (minScale * abs(position))*/
            page.scaleX = 1 - (minScale * Math.abs(position))

            // If you want a fading effect uncomment the next line:
            page.alpha = nextItemAlpha + (1 - Math.abs(position))
            page.elevation = -Math.abs(position)
        }
        viewPager.setPageTransformer(pageTransformer)

        //// The ItemDecoration gives the current (centered) item horizontal margin so that
        //// it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            viewPager.context,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        viewPager.addItemDecoration(itemDecoration)

    }

    override fun onResume() {
        super.onResume()


        autoScrollHandler.startAutoScroll()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            if (backPressedTime + backPressedInterval > System.currentTimeMillis()) {

                // Create an AlertDialog
                val builder = AlertDialog.Builder(binding.root.context)

                // Inflate the custom layout for the dialog
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.exit_dialog, null)

                val exitButton = dialogView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnExit)
                val cancelButton = dialogView.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnCancel)


                // Set the custom layout to the dialog
                builder.setView(dialogView)

                // Create and show the AlertDialog
                val dialog = builder.create()


                // Set click listeners for the buttons
                exitButton.setOnClickListener {
                    // Handle exit button click
                    activity?.finishAffinity()// Exit the app
                    dialog.dismiss() // Dismiss the dialog
                }

                cancelButton.setOnClickListener {
                    // Handle cancel button click
                    dialog.dismiss() // Dismiss the dialog
                }

                dialog.show()
            }
            backPressedTime = System.currentTimeMillis()

        }
    }

    override fun onPause() {
        super.onPause()
        autoScrollHandler.stopAutoScroll()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermission() {
        if (!PermissionUtils.hasVideoRecordingPermissions(binding.root.context)) {


            PermissionUtils.requestVideoRecordingPermission(binding.root.context, object :
                PermissionsCallback {
                override fun onPermissionRequest(granted: Boolean) {
                   /* if (!granted) {
                        dialogRecordingPermission()

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (!Environment.isExternalStorageManager()) {
                                dialogAllFileAccessPermissionAbove30()
                            }

                        }

                    }*/

                }

            })

        }
    }

    private fun dialogRecordingPermission() {
        PermissionUtils.createAlertDialog(
            binding.root.context,
            "Permission Denied!",
            "Go to setting and enable recording permission",
            "OK", ""
        ) { value ->
            if (value) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }

    fun dialogAllFileAccessPermissionAbove30() {
        PermissionUtils.createAlertDialog(
            binding.root.context,
            "All file permissions",
            "Go to setting and enable all files permission",
            "OK", ""
        ) { value ->
            if (value) {
                val getpermission = Intent()
                getpermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(getpermission)
            }
        }
    }

    fun electricStateList(){
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        loginResponse?.let { loginData ->
            loginData.userid?.let {
                val data = mapOf(
                    "userid" to it,
                    "ebilpay" to "electric",
                )
                val gson =  Gson();
                var jsonString = gson.toJson(data);
                loginData.AuthToken?.let {
                    viewModel?.electricStatelist(it,jsonString.encrypt())
                }
            }
        }
    }



}
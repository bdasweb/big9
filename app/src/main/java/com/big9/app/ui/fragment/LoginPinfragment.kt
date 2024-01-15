package com.big9.app.ui.fragment


import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.big9.app.R
import com.big9.app.adapter.PhonePadAdapter2
import com.big9.app.data.model.ReceiptModel
import com.big9.app.data.model.profile.Data
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentLoginPinBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.activity.RegActivity
import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.LoadingPopup
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.appUpdateUrl
import com.big9.app.utils.helpers.Constants.loginMobileReferanceNumber
import com.big9.app.utils.helpers.Constants.rmnForgotPassword
import com.big9.app.utils.helpers.PermissionUtils
import com.big9.app.utils.`interface`.KeyPadOnClickListner
import com.big9.app.utils.`interface`.PermissionsCallback
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlin.random.Random


class LoginPinfragment : BaseFragment() {
    lateinit var binding: FragmentLoginPinBinding
    var keyPad = ArrayList<Int>()
    var loadingPopup: LoadingPopup? = null
    private var loader: Dialog? = null
    private val myViewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_pin, container, false)
        binding.viewModel = myViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        onViewClick()
        observer()
    }

    private fun onViewClick() {
        binding.apply {
            tvSwitchAcc.setOnClickListener {
                activity?.let { act ->
                    sharedPreff.clearUserData()
                    val intent = Intent(act, RegActivity::class.java)
                    intent.putExtra("isForgotPin", false)
                    startActivity(intent)
                    act.finish()
                }

            }
            tvForgotPassword.setOnClickListener {
                /*activity?.let {act->
                    val intent = Intent(act, RegActivity::class.java)
                    intent.putExtra("isForgotPin",true)
                    startActivity(intent)
                    act.finish()
                }*/
                viewModel?.userProfileMobile?.value?.let {
                    rmnForgotPassword = it
                }

                findNavController().navigate(R.id.action_loginPinfragment_to_forgotPasswordOtpFragment)
            }


        }

    }

    override fun onResume() {
        super.onResume()

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
        myViewModel.loginPin.value = ""
        checkPermission()
        setKeyPad(binding.recyclePhonePad)
        binding.apply {
            recyclePhonePad.requestFocus()
            hideKeyBoard(firstPinView)
        }

        sharedPreff?.getUserData()?.let {
            setUserData(it)
        }


        //transition slip
        myViewModel.receiveStatus.value = ""
        myViewModel.receiveReceptMessahe.value = getString(R.string.transaction_slip)


        //Constants.recycleViewReceiptList.add(ReceiptModel("Transaction Id","300000025", type = 1))
        /*Constants.recycleViewReceiptList.add(ReceiptModel("Subscriber/ Customer Number","8583863153", type = 1))
        Constants.recycleViewReceiptList.add(ReceiptModel("Transaction Amount","₹10.00", type = 2))
        Constants.recycleViewReceiptList.add(ReceiptModel("Running Balance","₹200.22", type = 3))
        Constants.recycleViewReceiptList.add(ReceiptModel("Operator","AIRTEL", type = 4))
        Constants.recycleViewReceiptList.add(ReceiptModel("Operator ID","N/A", type = 1))*/

        /*val dialogFragment = ReceptDialogFragment()
        dialogFragment.show(childFragmentManager, dialogFragment.tag)*/

        /*val (isLogin, loginResponse) =sharedPreff.getLoginData()
        loginResponse?.let {loginData->


            val data = mapOf(
              "userid" to loginData.userid
            )

            val gson= Gson()
            var jsonString = gson.toJson(data)
            Log.d("TAG_otp_jsonString", "initView:jsonString "+jsonString)
            Log.d("TAG_otp_jsonString", "initView:encript "+jsonString.encrypt())
            //if (isLogin) {
               // loginData.AuthToken?.let {
                    myViewModel?.profile(auth,jsonString.encrypt())

                //}
           // }

        }*/
        callProfile()
    }

    private fun callProfile() {



        /*val (isLogin, loginResponse) =sharedPreff.getLoginData()


        loginResponse?.let {
            val data = mapOf(

                "userid" to it.userid,

                )
            val gson= Gson()
            var jsonString = gson.toJson(data)
            Log.d("TAG_p", "callProfile:json "+jsonString)
            Log.d("TAG_p", "callProfile:e \n"+jsonString.encrypt())
            loginResponse?.AuthToken?.let {
                var auth="eyJ1c2VyX2lkIjoiIiwidGltZXN0YW1wIjoxNzAxOTM5MTk2LCJyYW5kb20iOiJhN2ZjZWRjMjM1NzkxOGJlZDdjNjY2OGJjYmVhYmNhMmU4NWNhYWIwODE2ODg5ZjdhODY5YTY3MzBmNWY3Y2MzIn0="
                var data="ZqHbVba0bT2zVD2pxkGEH9tsoSvGvl18BD8ZpvkXvtM="
                myViewModel?.profile(auth,data)
               // myViewModel?.profile(it,jsonString.encrypt())
            }
        }*/

        /*"referenceid" to loginData.,*/

        //val a="eyJ1c2VyX2lkIjoiIiwidGltZXN0YW1wIjoxNzAxOTM5MTk2LCJyYW5kb20iOiJhN2ZjZWRjMjM1NzkxOGJlZDdjNjY2OGJjYmVhYmNhMmU4NWNhYWIwODE2ODg5ZjdhODY5YTY3MzBmNWY3Y2MzIn0="
        val (isLogin, loginResponse) = sharedPreff.getLoginData()
        loginResponse?.let { loginData ->

            loginMobileReferanceNumber = "com.big9.app" + generateRandomNumberInRange().toString()
            val data = mapOf(
                "otp" to myViewModel?.loginPin?.value,
                "userid" to loginData.userid,

                "deviceid" to MethodClass.deviceUid(binding.root.context),
                "ipaddress" to MethodClass.getLocalIPAddress(),
                "location" to "123",
                "referenceid" to loginMobileReferanceNumber,
                "Timestamp" to MethodClass.getCurrentTimestamp(),
                "app_version" to  MethodClass.appVersion(binding.root.context)
            )
            /*"referenceid" to loginData.,*/
            val gson = Gson()
            var jsonString = gson.toJson(data)


            loginData.AuthToken?.let {
                myViewModel?.profile2(it, jsonString.encrypt())
            }


        }


    }


    fun setKeyPad(PhonePad: RecyclerView) {
        keyPad.clear()
        keyPad.add(1)
        keyPad.add(2)
        keyPad.add(3)
        keyPad.add(4)
        keyPad.add(5)
        keyPad.add(6)
        keyPad.add(7)
        keyPad.add(8)
        keyPad.add(9)
        keyPad.add(10)
        keyPad.add(0)
        keyPad.add(11)
        PhonePad.apply {

            adapter = PhonePadAdapter2(keyPad, object : KeyPadOnClickListner {
                override fun onClick(item: Int) {
                    //myViewModel.loginPin.value?.let {
                    if (item <= 9) {
                        if (myViewModel.loginPin.value?.length != 6) {
                            val loginPin = "${myViewModel.loginPin.value}$item"
                            myViewModel.loginPin.value = loginPin
                            if (myViewModel.loginPin.value?.length == 6) {


                                val (isLogin, loginResponse) = sharedPreff.getLoginData()
                                loginResponse?.let { loginData ->


                                    val data = mapOf(
                                        "userid" to loginData.userid,
                                        "mpin" to myViewModel.loginPin.value,
                                        "app_version" to  MethodClass.appVersion(binding.root.context)
                                    )

                                    val gson = Gson()
                                    var jsonString = gson.toJson(data)


                                    loginData.AuthToken?.let {
                                        myViewModel?.patternLogin(it, jsonString.encrypt())
                                    }
                                }
                                //This code need to delete.
                                //After testing remove this code
                                /*if (myViewModel.loginPin.value=="123456") {
                                    findNavController().navigate(com.big9.app.R.id.action_loginPinfragment_to_homeFragment2)
                                }*/
                            }

                        }
                    } else {
                        if (myViewModel.loginPin.value?.isNotEmpty() == true) {
                            myViewModel.loginPin.value = this.toString().substring(
                                0,
                                myViewModel.loginPin.value?.length?.minus(1) ?: 0
                            )
                        }

                    }
                    //}
                }

            })
            isNestedScrollingEnabled = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermission() {
        if (!PermissionUtils.hasVideoRecordingPermissions(binding.root.context)) {


            PermissionUtils.requestVideoRecordingPermission(binding.root.context, object :
                PermissionsCallback {
                override fun onPermissionRequest(granted: Boolean) {
                    /*if (!granted) {
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

    private fun observer() {
        myViewModel?.profile2Response?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    // loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    it.data?.data?.let {
                        sharedPreff?.setUserInfoData(it)
                        setUserData(it)
                    }


                    //  Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                   /* if (it.errorCode == 105) {
                        val (isLogin, loginResponse) = sharedPreff.getLoginData()
                        loginResponse?.let { loginData ->


                            val data = mapOf(
                                "userid" to loginData.userid,
                                "clientid" to Constants.CLIENT_ID,
                                "secretkey" to Constants.API_KEY,
                            )
                            *//*"referenceid" to loginData.,*//*
                            val gson = Gson()
                            var jsonString = gson.toJson(data)


                            loginData.AuthToken?.let {
                                myViewModel?.refreshToken(it, jsonString.encrypt())
                            }
                        }
                    }*/
                    if (it.errorCode == 103) {
                        val (isLogin, loginResponse) = sharedPreff.getLoginData()
                        loginResponse?.let { loginData ->


                            val data = mapOf(
                                "userid" to loginData.userid,
                                "app_version" to  MethodClass.appVersion(binding.root.context)
                            )
                            /*"referenceid" to loginData.,*/
                            val gson = Gson()
                            var jsonString = gson.toJson(data)


                            loginData.AuthToken?.let {
                                myViewModel?.appUpdate(it, jsonString.encrypt())
                            }
                        }
                    }



                    else {
                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    }
                }
            }
        }

        myViewModel?.patternLoginReceptLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    // loader?.show()
                }

                is ResponseState.Success -> {
                    myViewModel.loginPin.value = ""
                    findNavController().navigate(com.big9.app.R.id.action_loginPinfragment_to_homeFragment2)
                    myViewModel?.patternLoginReceptLiveData?.value = null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    myViewModel.loginPin.value = ""
                    /*if (it.errorCode == 105) {

                        val (isLogin, loginResponse) = sharedPreff.getLoginData()
                        loginResponse?.let { loginData ->


                            val data = mapOf(
                                "userid" to loginData.userid,
                                "clientid" to Constants.CLIENT_ID,
                                "secretkey" to Constants.API_KEY,
                            )
                            *//*"referenceid" to loginData.,*//*
                            val gson = Gson()
                            var jsonString = gson.toJson(data)


                            loginData.AuthToken?.let {
                                myViewModel?.refreshToken(it, jsonString.encrypt())
                            }
                        }
                    }*/
                   // else {

                  //  }

                    if (it.errorCode == 103) {
                        val (isLogin, loginResponse) = sharedPreff.getLoginData()
                        loginResponse?.let { loginData ->


                            val data = mapOf(
                                "userid" to loginData.userid,
                                "app_version" to  MethodClass.appVersion(binding.root.context)
                            )
                            /*"referenceid" to loginData.,*/
                            val gson = Gson()
                            var jsonString = gson.toJson(data)


                            loginData.AuthToken?.let {
                                myViewModel?.appUpdate(it, jsonString.encrypt())
                            }
                        }
                    }
                    else {
                        handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    }
                    myViewModel?.patternLoginReceptLiveData?.value = null
                }
            }
        }

        //
        myViewModel?.refreshTokenResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                    val (isLogin, loginResponse) = sharedPreff.getLoginData()
                    loginResponse?.let { lData ->


                        /*
                        authViewModel.panCardNo?.value.let {
                            lData?.pancard=it
                        }
                        */
                        sharedPreff?.setLoginData(loginResponse, true, "ACTIVE")

                    }
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


        myViewModel?.appupdateResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                    Log.d("TAGupdate", "observer: 1")
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    Log.d("TAGupdate", "observer: 2")
                    appUpdateUrl=it?.data?.appUpdateUrl
                    it?.data?.appUpdateUrl?.let {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    }

                }

                is ResponseState.Error -> {
                    Log.d("TAGupdate", "observer: 3")
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }




    }

    private fun setUserData(data: Data) {
        data.SelfieImageData?.let {
            val decodedString: ByteArray = Base64.decode(it, Base64.DEFAULT)
            val decodedByte =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            Glide.with(this)
                .asBitmap() // Use asBitmap() instead of asGif()
                .load(decodedByte)
                .error(R.drawable.ic_success) // Set the default image resource

                .into(binding.profileImage)
        }
        myViewModel.apply {
            userProfileMobile.value = data.mobileNo
            userProfileName.value = data.name
        }
    }

    /* private fun setObserver() {
         myViewModel?.profileResponse?.observe(viewLifecycleOwner){
             when (it) {
                 is ResponseState.Loading -> {
                     loadingPopup?.show()
                 }

                 is ResponseState.Success -> {
                       loadingPopup?.dismiss()
                     Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                     }

                 is ResponseState.Error -> {
                     //   loadingPopup?.dismiss()
                     handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                 }
             }
         }
     }*/


    fun generateRandomNumberInRange(): Int {
        return Random.nextInt(1000, 9999)
    }
}
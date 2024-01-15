package com.big9.app.ui.fragment.regandkyc
import android.provider.Settings

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.R
import com.big9.app.adapter.PhonePadAdapter
import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.FragmentOtpMobileBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.activity.AuthenticationActivity
import com.big9.app.ui.activity.DashboardActivity
import com.big9.app.ui.activity.RegActivity
import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.LoadingPopup
import com.big9.app.utils.*
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.common.MethodClass.getCurrentTimestamp
import com.big9.app.utils.common.MethodClass.getLocalIPAddress
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.API_KEY
import com.big9.app.utils.helpers.Constants.CLIENT_ID

import com.big9.app.utils.helpers.Constants.loginMobileNumber
import com.big9.app.utils.helpers.Constants.loginMobileReferanceNumber
import com.big9.app.utils.helpers.OtpRetriever
import com.big9.app.utils.`interface`.KeyPadOnClickListner
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


class OtpMobileFragment : BaseFragment()  {
    lateinit var binding: FragmentOtpMobileBinding
    var keyPad = ArrayList<Int>()
    private lateinit var oTORetriever: OtpRetriever
    val MY_PERMISSIONS_REQUEST_LOCATION=1
    private  val PERMISSION_REQUEST_LOCATION = 100
    private val authViewModel: AuthViewModel by activityViewModels()
    var isForgotPinPage=false
    val jsonDataLocation=JsonObject()
    private var loader: Dialog? = null
    var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        getActivityResult(result)
    }
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    var loadingPopup: LoadingPopup? = null
    //private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp_mobile, container, false)
        binding.viewModel=authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oTORetriever=OtpRetriever()

        init()
        setKeyPad(binding.recyclePhonePad2)
        onViewClick()
        observer()
        startSmartUserConsent()
        isForgotPinPage= arguments?.getBoolean("isForgotPin") == true
    }

    private fun observer() {
        authViewModel?.otpResponse?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()


                    authViewModel.otp.value=""
                    //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                    if (it.data?.step==null){
                        (activity as? RegActivity)?.let {
                            val intent=Intent(requireActivity(), DashboardActivity::class.java)

                            startActivity(intent)
                            it.finish()


                       // if(authViewModel.otp.value=="123456"){
                                    //binding.lottieTickAnim.visibility=View.VISIBLE
                                   /* binding.lottieConfettiAnim.visibility=View.VISIBLE
                                    if (!isForgotPinPage) {
                                        it.data?.step?.let {
                                            setdata2(it)
                                        }

                                    }
                                    else{
                                        activity?.let {act->
                                            val intent = Intent(act, DashboardActivity::class.java)
                                            startActivity(intent)
                                            act.finish()
                                        }*/
                                    }
                                    //findNavController().navigate(R.id.action_otpFragment_to_congratulationFragment)
                                    // Toast.makeText(requireContext(), "match", Toast.LENGTH_SHORT).show()
                              //  }
                    }
                    else  {
                        activity?.let {act->
                            try {
                                it.data?.step?.let {
                                    val stape=it.toInt()
                                    val intent=Intent(act, AuthenticationActivity::class.java)
                                    intent.putExtra(Constants.stape,stape)
                                    startActivity(intent)
                                    act.finish()
                                }

                            }catch (e:Exception){}
                            /*val intent=Intent(act, AuthenticationActivity::class.java)
                            intent.putExtra(stape,it.data?.step)
                            startActivity(intent)
                            act.finish()*/
                        }
                    }


                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    authViewModel.otp.value=""
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


        authViewModel?.authLogin?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    //loadingPopup?.show()
                    loader?.let { it.show() }
                }

                is ResponseState.Success -> {
                    //  loadingPopup?.dismiss()
                    loader?.let { it.dismiss() }
                    cownDown()
                    Toast.makeText(requireContext(), ""+it?.data?.Description, Toast.LENGTH_SHORT).show()

                    authViewModel?.authLogin?.value=null

                }

                is ResponseState.Error -> {
                    //   loadingPopup?.dismiss()
                    loader?.let { it.dismiss() }

                    authViewModel?.authLogin?.value=null
                }
            }
        }
    }

    fun init(){
        activity?.let {act->

            //fusedLocationClient = LocationServices.getFusedLocationProviderClient(act)
            activity?.let {  mFusedLocationClient = LocationServices.getFusedLocationProviderClient(it)}
            loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
            checkPermission2(act)
        }

       //activity?.let {  mFusedLocationClient = LocationServices.getFusedLocationProviderClient(it)}

    }

    private fun checkPermission2(act: FragmentActivity) {
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                act,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        } else {
            // Permission already granted, start location updates
            if (isGpsEnabled()==true) {
                startLocationUpdates()
                //userLocation()
            }
            else{
                showEnableGPSDialog()
            }
        }
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {act->
                checkPermission2(act)
          }
        }
        else {
            getLocation()
            /*fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Use the location object to retrieve the location data
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val geocoder = Geocoder(binding.root.context, Locale.getDefault())
                        val addresses: List<Address>? =
                            geocoder.getFromLocation(latitude, longitude, 1)

                        // Process the addresses if available
                        addresses?.let {
                            if (it.isNotEmpty()) {
                                val address: Address = it[0]
                                *//* val subAdminArea = address.subAdminArea
                            val continentName = address.countryName
                            val city = address.locality
                            val state = address.adminArea
                            val postalCode = address.postalCode
                            val knownName = address.featureName
                            val subLocality = address.subLocality
                            val addressLine1 = address.getAddressLine(0)
                            val addressLine2 = address.getAddressLine(1)*//*


                                address?.let {
                                    jsonDataLocation.addProperty("subAdminArea", it.subAdminArea)
                                    jsonDataLocation.addProperty("continentName", it.countryName)

                                    jsonDataLocation.addProperty("city", it.locality)
                                    jsonDataLocation.addProperty("state", it.adminArea)

                                    jsonDataLocation.addProperty("postalCode", it.postalCode)
                                    jsonDataLocation.addProperty("knownName", it.featureName)
                                    jsonDataLocation.addProperty("latitude", it.latitude)
                                    jsonDataLocation.addProperty("longitude", it.longitude)

                                    jsonDataLocation.addProperty("subLocality", it.subLocality)
                                    jsonDataLocation.addProperty("address1", it.getAddressLine(1))
                                    jsonDataLocation.addProperty("address2", it.getAddressLine(2))
                                }


                                // Now you have the location data, you can use it as needed
                                // For example, update UI with the location details
                            }
                        }
                    }
                }*/
        }
    }

    private fun userLocation() {
        // Check for runtime permissions
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }

        } else {
            // Permission already granted
            // Proceed with getting location
            getLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        activity?.let {act->
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(act)


            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude



                        val accuracy = location.accuracy

                        jsonDataLocation.addProperty("locationAccuracyRadius",accuracy)

                        getFullLocation(latitude,longitude)

                        // Do something with the latitude and longitude

                    } else {
                        // Handle the case where the location is null
                    }
                }
                .addOnFailureListener { e ->
                    // Handle errors that may occur while retrieving the location
                    //Log.e("Location", "Error getting location", e)
                }
        }


    }





    fun getFullLocation(latitude: Double, longitude: Double) {
        val defaultLocale = Locale.getDefault()
        Currency.getInstance( Locale("en", "IN")).apply {
            jsonDataLocation.addProperty("currencyCode",currencyCode)
            jsonDataLocation.addProperty("currencySymbol",symbol)
        }
        TimeZone.getDefault()?.let {
            jsonDataLocation.addProperty("timezone",it.id)
        }
        /*
        timezone
        currencyCode
        currencySymbol
        subAdminArea
        locationAccuracyRadius
        continentName
        */

        val geocoder: Geocoder
        val addresses: List<Address>?
        geocoder = Geocoder(binding.root.context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


       /* val address: String =
            addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city: String = addresses!![0].locality
        val state: String = addresses!![0].adminArea
        val country: String = addresses!![0].countryName
        val postalCode: String = addresses!![0].postalCode
        val knownName: String = addresses!![0].featureName*/
        addresses?.get(0)?.let {

            jsonDataLocation.addProperty("subAdminArea",it.subAdminArea)
            jsonDataLocation.addProperty("continentName",it.countryName)

            jsonDataLocation.addProperty("city",it.locality)
            jsonDataLocation.addProperty("state",it.adminArea)

            jsonDataLocation.addProperty("postalCode",it.postalCode)
            jsonDataLocation.addProperty("knownName",it.featureName)
            jsonDataLocation.addProperty("latitude",it.latitude)
            jsonDataLocation.addProperty("longitude",it.longitude)

            jsonDataLocation.addProperty("subLocality",it.subLocality)
            jsonDataLocation.addProperty("address1",it.getAddressLine(1))
            jsonDataLocation.addProperty("address2",it.getAddressLine(2))
        }

    }

   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLocation()
                } else {
                    // Permission denied
                    // Handle accordingly (e.g., show a message)
                }
            }
        }
    }*/

    private fun onViewClick() {
        binding?.apply {
            binding.tdResendOtp.setOnClickListener {
                viewModel?.otp?.value=""
               if (binding.tdResendOtp.text.toString().trim()=="Resend OTP"){
                   val data = mapOf(
                       "clientid" to CLIENT_ID,
                       "secretkey" to API_KEY,
                       "mobile" to loginMobileNumber,
                       "refid" to loginMobileReferanceNumber,
                       "token" to sharedPreff.getFcnToken().toString(),
                       "app_version" to  MethodClass.appVersion(binding.root.context)
                   )
                   val gson= Gson()
                   var jsonString = gson.toJson(data)
                   viewModel?.authLoginRegistration(jsonString.encrypt())
               }
            }

            tvSwitchAcc.setOnClickListener{
              findNavController().popBackStack()
            }
            btnConfirmOtp.setOnClickListener {
                authViewModel?.otp?.value?.apply {
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    loginResponse?.let {loginData->


                        val data = mapOf(
                            "otp" to authViewModel.otp.value,
                            "userid" to loginData.userid,

                            "deviceid" to MethodClass.deviceUid(binding.root.context),
                            "ipaddress" to getLocalIPAddress(),
                            "location" to jsonDataLocation.toString(),
                            "referenceid" to loginMobileReferanceNumber,
                            "Timestamp" to getCurrentTimestamp(),
                            "token" to sharedPreff.getFcnToken().toString()
                        )
                        /*"referenceid" to loginData.,*/
                        val gson= Gson()
                        var jsonString = gson.toJson(data)
                        if (this.length==6) {
                        loginData.AuthToken?.let {
                            authViewModel?.sendOtp(it,jsonString.encrypt())
                        }
                    }

                    }
                }

            }
        }

        }
    fun setKeyPad(PhonePad: RecyclerView) {
        authViewModel.mobError.value=""
        cownDown()
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
            //authViewModel.keyPadValue.value=getString(R.string.mobile_no_hint)
            adapter= PhonePadAdapter(keyPad,object : KeyPadOnClickListner {
                override fun onClick(item: Int) {
                    authViewModel.otp.value?.apply {
                        if (item<=9 ) {
                            if (!isGpsEnabled()) {
                                showEnableGPSDialog()
                            }
                            else if (this.length!=6) {
                                authViewModel.otp.value = "${this}${item}"

                                // binding.firstPinView.text=this

                                /*if(authViewModel.otp.value=="123456"){
                                    //binding.lottieTickAnim.visibility=View.VISIBLE
                                    binding.lottieConfettiAnim.visibility=View.VISIBLE
                                    if (!isForgotPinPage) {
                                        setdata2()
                                    }
                                    else{
                                        activity?.let {act->
                                            val intent = Intent(act, DashboardActivity::class.java)
                                            startActivity(intent)
                                            act.finish()
                                        }
                                    }
                                    //findNavController().navigate(R.id.action_otpFragment_to_congratulationFragment)
                                    // Toast.makeText(requireContext(), "match", Toast.LENGTH_SHORT).show()
                                }*/

                                //binding.firstPinView.setText(authViewModel.otp.value)
                            }
                        }
                        else if(item==10){
                            //authViewModel.keyPadValue.value =""
                        }
                        else {
                            if (this.isNotEmpty()) {
                                authViewModel.otp.value = this.toString().substring(0, this.length - 1)
                                //binding.firstPinView.setText(authViewModel.otp.value)

                            }

                        }
                    }
                }

            })
            isNestedScrollingEnabled=false
        }
    }

    private fun setdata2(step: Int) {
        //binding.lottieTickAnim.setAnimationFromUrl(Constants.LOTTIE_TICK_LINK)
        //binding.lottieTickAnim.playAnimation()
        binding.lottieConfettiAnim.setAnimationFromUrl(Constants.LOTTIE_CONFETTIE_LINK)
        //        lottie_anim.setSpeed(0.7f);
        binding.lottieConfettiAnim.playAnimation()
        binding.lottieConfettiAnim.addAnimatorListener(
            object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    lifecycleScope.launch {
                        //delay(500L)
                        (activity as? RegActivity)?.let {
                            val intent=Intent(requireActivity(), DashboardActivity::class.java)
                            //val intent=Intent(requireActivity(), AuthenticationActivity::class.java)
                            intent.putExtra("stape",step.toString())
                            startActivity(intent)
                            it.finish()
                        }
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                startLocationUpdates()
            } else {
                // Permission denied, handle accordingly (e.g., show a message or request again)
                if(!isGpsEnabled()) {
                    showEnableGPSDialog()

                }

            }
        }
    }
    /*private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", binding.root.context.packageName, null)
        intent.data = uri
        startActivity(intent)
    }*/
    fun isGpsEnabled(): Boolean {
        binding.root.context?.let {context->
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
       return false
    }

    fun showEnableGPSDialog() {
        val alertDialog = AlertDialog.Builder(binding.root.context)
        alertDialog.apply {
            setTitle("Enable GPS")
            setMessage("GPS is required for this app. Please enable GPS.")
            setPositiveButton("Enable GPS") { dialog: DialogInterface, which: Int ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }
            setCancelable(false) // Prevent dismissing dialog on outside touch or back press
            create()
            show()
        }
    }
    fun cownDown() {


        val totalTimeInMillis: Long = TimeUnit.SECONDS.toMillis(120) // 60 seconds countdown

        var countDownTimer= object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                //authViewModel.timingValue.value = getString(R.string.resend_otp,secondsRemaining)/*"Resend OTP after <font color='#B80A13'>$secondsRemaining</font> second"*/
                //binding.tdResendOtp.text = getString(R.string.resend_otp,secondsRemaining)/*"Resend OTP after <font color='#B80A13'>$secondsRemaining</font> second"*/
                setTimerValue(secondsRemaining)
            }

            override fun onFinish() {
                val spannableString = SpannableStringBuilder()
                //authViewModel.timingValue.value =  "Resend OTP"
                //binding.tdResendOtp.text =  "Resend OTP"
                val firstPart = "Resend OTP"
                spannableString.append(firstPart)
                spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.pink2)),
                    spannableString.length - firstPart.length,
                    spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.tdResendOtp.text =spannableString
            }
        }


        countDownTimer.start()

    }
    private fun setTimerValue(secondsRemaining: Long) {
        // Create a SpannableStringBuilder
        val spannableString = SpannableStringBuilder()

        // Add the first part of the text with one color
        val firstPart = "Resend OTP after "
        spannableString.append(firstPart)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
            spannableString.length - firstPart.length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Add the second part of the text with a different color
        val secondPart = "$secondsRemaining"
        spannableString.append(secondPart)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.pink2)),
            spannableString.length - secondPart.length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.append(" second")
        // Set the SpannableStringBuilder as the text for the TextView
        binding.tdResendOtp.text = spannableString
    }


    override fun onStart() {
        super.onStart()
        activity?.let {
            val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
            it.registerReceiver(oTORetriever, intentFilter)
        }

    }

    override fun onStop() {
        super.onStop()
        activity?.let {
            it.unregisterReceiver(oTORetriever)
        }
    }


    private fun startSmartUserConsent() {
        /*activity?.let {
            val client = SmsRetriever.getClient(it)
            client.startSmsUserConsent(SMS_SENDER_NUMBER)
        }*/

    }
    private fun getActivityResult(result: ActivityResult?) {
        if (result?.resultCode == RESULT_OK) {
            val message: String? = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            message?.let{getOtpFromMessage(it)}
        }
    }
    /*override fun onSuccess(intent: Intent?) {
        resultLauncher.launch(intent)
    }*/
    private fun getOtpFromMessage(message: String) {
        val otpPattern: Pattern = Pattern.compile("(|^)\\d{4}")
        val matcher: Matcher = otpPattern.matcher(message)
        if (matcher.find()) {
            try {

            }catch (e:Exception){}

        }
    }
}



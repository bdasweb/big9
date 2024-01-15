package com.big9.app.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.big9.app.R
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.ActivityDashboardBinding
import com.big9.app.ui.base.BaseActivity
import com.big9.app.ui.popup.ErrorPopUp
import com.big9.app.ui.popup.LoadingPopup
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.isAfterReg
import com.big9.app.utils.helpers.Constants.isRecept
import com.big9.app.utils.helpers.RequestBodyHelper
import com.big9.app.utils.helpers.ScreenshotUtils.Companion.takeScreenshot
import com.big9.app.utils.helpers.SharedPreff
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class DashboardActivity : BaseActivity() {

    private lateinit var appUpdateManager: AppUpdateManager

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                // Handle update failure or user cancellation here
            }
        }
    lateinit var binding: ActivityDashboardBinding
    private lateinit var myViewModel: MyViewModel

    private var navController: NavController? = null

    @Inject
    lateinit var requestBodyHelper: RequestBodyHelper
    private val REQUEST_MEDIA_PROJECTION = 1

    @Inject
    lateinit var sharedPreff: SharedPreff

    var loadingPopup: LoadingPopup? = null
    var errorPopUp: ErrorPopUp? = null
    private val mTag = "tag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]


        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        intent?.let { intentData ->
            val isReceptBooleanValue = intentData.getBooleanExtra(isRecept, false)
            val isAfterRegVal = intentData.getBooleanExtra(isAfterReg, false)
            if (isReceptBooleanValue) {

            }
            if (isAfterRegVal) {
                navController?.navigate(R.id.homeFragment2)
                return
            }}

        init()

        observer()

        loadingPopup = LoadingPopup(this)
        errorPopUp = ErrorPopUp(this)

        // myViewModel.login(requestBodyHelper.getLoginRequest("test@abc.com", "123"))

    }

    fun init() {


        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                CoroutineScope(Dispatchers.Main).launch {
                    /* val navHostFragment: NavHostFragment =
                         supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                     navController = navHostFragment.navController
                     intent?.let { intentData ->
                         val isReceptBooleanValue = intentData.getBooleanExtra(isRecept, false)
                         val isAfterRegVal = intentData.getBooleanExtra(isAfterReg, false)
                         if (isReceptBooleanValue) {

                         }
                         if (isAfterRegVal) {
                             navController?.navigate(R.id.homeFragment2)
                         }
                     }*/

                    var currentFragmentId = navController?.currentDestination?.id
                    if (currentFragmentId == R.id.homeFragment) {
                        binding.bottomNav.visibility = View.GONE
                        binding.clHeader.visibility = View.GONE
                    } else {
                        binding.bottomNav.visibility = View.GONE
                        binding.clHeader.visibility = View.GONE
                    }
                }
            }
        }

    }
    private fun checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkForAppUpdate()
    }
    fun observer() {

        /* myViewModel.loginResponseLiveData.observe(this) {

             when (it) {
                 is ResponseState.Loading -> {
                     //loadingPopup?.show()
                 }

                 is ResponseState.Success -> {
                     loadingPopup?.dismiss()
                     //  Toast.makeText(this, ""+it?.data?.response?.data?.get(0)?.name, Toast.LENGTH_SHORT).show()
                     //var a=it.data?.response?.data?.get(0)?.name?.encryptData("ttt")
                     // var a=it?.data?.response?.data?.get(0)?.name
                     //var b=a?.decryptData("ttt")
                     //Toast.makeText(this, "$b", Toast.LENGTH_SHORT).show()
                 }

                 is ResponseState.Error -> {
                     loadingPopup?.dismiss()
                     handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                 }
             }
         }*/
    }

    fun navigate() {
        //navController?.navigate(R.id.homeFragment2)
        //navController?.popBackStack(R.id.homeFragment2,false)

        startActivity(Intent(this, DashboardActivity::class.java).putExtra(Constants.isAfterReg,true))

    }

    fun shareImage(screenshotBitmap: Bitmap) {
        takeScreenshot(this, screenshotBitmap)
        /* val mediaProjectionManager =
             getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
         startActivityForResult(
             mediaProjectionManager.createScreenCaptureIntent(),
             REQUEST_MEDIA_PROJECTION
         )*/
    }


}
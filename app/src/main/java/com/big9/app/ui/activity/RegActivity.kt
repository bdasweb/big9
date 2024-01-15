package com.big9.app.ui.activity

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.big9.app.R

import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.ActivityRegBinding
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegBinding
    private lateinit var authViewModel: AuthViewModel
    private var navController: NavController? = null
    val  bundle=Bundle()
    private lateinit var appUpdateManager: AppUpdateManager

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                // Handle update failure or user cancellation here
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_reg)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Hide the navigation bar and make the content layout full-screen
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                )
        //setContentView(R.layout.activity_auth)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reg)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.viewModel = authViewModel
        binding.lifecycleOwner = this

        init()

    }
    override fun onResume() {
        super.onResume()
        checkForAppUpdate()
    }
    fun init(){
        bundle.putBoolean("isForgotPin",false)
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        if (intent.getBooleanExtra("isAlreadyLogin",false)){

            binding.navHostFragment.visibility = View.VISIBLE
           // setSecondAnimation()
        }
        if (intent.getBooleanExtra("isForgotPin",false))
        {
            setSecondAnimation()
            binding.navHostFragment.visibility = View.VISIBLE
            bundle.putBoolean("isForgotPin",true)
            navController?.navigate(R.id.action_loginMobileFragment_to_otpMobileFragment,bundle)
        }

        else {
            binding.navHostFragment.visibility = View.GONE
            setdata()
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
    private fun setdata() {

        //binding.animationView.setAnimationFromUrl(Constants.LOTTIE_CONFETTIE_LINK)
        //        lottie_anim.setSpeed(0.7f);
        binding.animationView.apply {
            setAnimation(R.raw.bg_login)

            playAnimation()
            addAnimatorUpdateListener { animation ->
                var duration:Float=animation.animatedFraction
                if (duration>0.5f){
                   // binding.consLogin.visibility=View.VISIBLE
                   // binding.imgLogo.visibility=View.VISIBLE
                }
            }


            addAnimatorListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        lifecycleScope.launch {
                            delay(1000L)
                            binding.navHostFragment.visibility = View.VISIBLE
                        }
                    }
                    override fun onAnimationEnd(animation: Animator) {
                        lifecycleScope.launch {
                            // binding.imgLogo.visibility=View.GONE
                            //binding.consLogin.visibility=View.VISIBLE
                            visibility=View.GONE
                            binding.constLayout.setBackgroundResource(R.color.gray)
                            /*delay(500L)
                            startActivity(
                                Intent(
                                    this@CongratulationsActivity,
                                    DashBoardActivity::class.java
                                )
                            )
                            finish()*/
                            /*ObjectAnimator.ofFloat(binding.imgLogo, "translationY", 100f).apply {
                                duration = 2000
                                start()
                            }*/

                            setSecondAnimation()

                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
        }
    }
    private fun setSecondAnimation() {
        binding.animationViewHeader.apply {
            setAnimation(R.raw.login_header_bg)
            playAnimation()
            addAnimatorListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        binding.imgLogo.visibility=View.VISIBLE
                        binding.tvWelcometext.visibility=View.VISIBLE
                    }
                    override fun onAnimationEnd(animation: Animator) {
                        lifecycleScope.launch {
                            // binding.imgLogo.visibility=View.GONE
                            //binding.consLogin.visibility=View.VISIBLE
                            visibility=View.GONE
                            binding.constLayout.setBackgroundResource(R.color.gray)
                            /*delay(500L)
                            startActivity(
                                Intent(
                                    this@CongratulationsActivity,
                                    DashBoardActivity::class.java
                                )
                            )
                            finish()*/
                           // binding.tvWelcometext.visibility=View.VISIBLE
//                            ObjectAnimator.ofFloat(binding.imgLogo, "translationY", 100f).apply {
//                                duration = 2000
//                                start()
//                            }
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
        }
    }


}
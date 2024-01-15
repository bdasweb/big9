package com.big9.app.ui.activity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.big9.app.R

import com.big9.app.adapter.PhonePadAdapter
import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.ActivityAuthBinding

import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.`interface`.KeyPadOnClickListner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    private lateinit var authViewModel: AuthViewModel
    var keyPad = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the app to full-screen mode
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.viewModel = authViewModel
        binding.lifecycleOwner = this

        setKeyPad(binding.recyclePhonePad)
        setKeyPad(binding.recyclePhonePad2)
        setdata()
        viewOnClick()
        binding.recyclePhonePad2.visibility=View.GONE
    }

    private fun viewOnClick() {
        binding.apply {
            tvSwitchAcc.setOnClickListener {
                binding.recyclePhonePad2.visibility=View.VISIBLE
                binding.recyclePhonePad.visibility=View.GONE
                binding.consLogin.visibility=View.GONE
                binding.consOtp.visibility=View.VISIBLE
                binding.imgLogo.visibility=View.VISIBLE
                binding.tvWelcometext.visibility=View.VISIBLE

                binding.recyclePhonePad2.visibility=View.GONE
                binding.recyclePhonePad.visibility=View.VISIBLE
            }
            pinView.setOnClickListener {

            }

            btnConfirmLocation.setOnClickListener {
                authViewModel.mobError.value=""
                if (viewModel?.keyPadValue?.value?.length==10){

                    binding.recyclePhonePad2.visibility=View.VISIBLE
                    binding.recyclePhonePad.visibility=View.GONE
                    binding.consLogin.visibility=View.GONE
                    binding.consOtp.visibility=View.VISIBLE
                    binding.imgLogo.visibility=View.VISIBLE
                    binding.tvWelcometext.visibility=View.VISIBLE
                }
                else{
                    binding.recyclePhonePad2.visibility=View.GONE
                    binding.recyclePhonePad.visibility=View.VISIBLE
                    authViewModel.mobError.value="Please enter a valid mobile number."
                }

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
                    binding.consLogin.visibility=View.VISIBLE
                    binding.imgLogo.visibility=View.VISIBLE
                }
            }


            addAnimatorListener(
            object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

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
                            binding.tvWelcometext.visibility=View.VISIBLE
                            ObjectAnimator.ofFloat(binding.imgLogo, "translationY", 100f).apply {
                                duration = 2000
                                start()
                            }



                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
        }
    }

    fun setKeyPad(PhonePad: RecyclerView) {
        authViewModel.mobError.value=""
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
                    if (binding.consLogin.isVisible){
                        authViewModel.mobError.value = ""
                    authViewModel.keyPadValue.value?.apply {
                        if (item <= 9) {
                            if (this.length != 10) {
                                authViewModel.keyPadValue.value = "${this}$item"
                            }
                        } else if (item == 10) {
                            //authViewModel.keyPadValue.value =""
                        } else {
                            if (this.isNotEmpty()) {
                                authViewModel.keyPadValue.value = this.substring(0, this.length - 1)
                            }

                        }
                    }
                }
               else{
                        authViewModel.otp.value?.apply {
                            if (item<=9 ) {
                                if (this.length!=6) {

                                    // binding.firstPinView.text=this
                                    authViewModel.otp.value = "${this}${item}"
                                    if(authViewModel.otp.value=="123456"){
                                        //binding.lottieTickAnim.visibility=View.VISIBLE
                                        binding.lottieConfettiAnim.visibility=View.VISIBLE
                                        setdata2()
                                        //findNavController().navigate(R.id.action_otpFragment_to_congratulationFragment)
                                        // Toast.makeText(requireContext(), "match", Toast.LENGTH_SHORT).show()
                                    }

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
                }

            })
            isNestedScrollingEnabled=false
        }
    }

    private fun setdata2() {
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

                            startActivity(Intent(this@AuthActivity, AuthenticationActivity::class.java))
                            finish()

                    }
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {


    }
}
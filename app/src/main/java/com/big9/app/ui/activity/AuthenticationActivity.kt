package com.big9.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.big9.app.R
import com.big9.app.data.viewMovel.AuthViewModel
import com.big9.app.databinding.ActivityAuthenticationBinding
import com.big9.app.utils.helpers.Constants.isAfterReg
import com.big9.app.utils.helpers.Constants.isRecept
import com.big9.app.utils.helpers.Constants.stape
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    private var navController: NavController? = null
    lateinit var binding: ActivityAuthenticationBinding
    var onBoundStape:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_authentication)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        var currentFragmentId = navController?.currentDestination?.id
        if (currentFragmentId==R.id.cameraFragment){
            binding.g1.visibility= View.GONE
        }
        else{
            binding.g1.visibility= View.VISIBLE
        }

        intent?.let {intentData->
            val isReceptBooleanValue=intentData.getBooleanExtra(isRecept,false)
            val isAfterRegVal=intentData.getBooleanExtra(isAfterReg,false)
            onBoundStape=intentData.getIntExtra(stape,0)
            if (isReceptBooleanValue){

            }
            if (isAfterRegVal){
                navController?.navigate(R.id.homeFragment2)
            }

            if (onBoundStape==1){
                navController?.navigate(R.id.regFragment)
            }
            if (onBoundStape==2){
                navController?.navigate(R.id.kycDetailsFragment)
            }
            if (onBoundStape==3){
                navController?.navigate(R.id.bankDetailsFragment)
            }
            if (onBoundStape==4){
                navController?.navigate(R.id.docuploadFragment)
            }
        }

        binding.imgLogout.setOnClickListener{
                val intent=Intent( this@AuthenticationActivity, RegActivity::class.java)
                startActivity(intent)
                finish()

        }

    }


    override fun onBackPressed() {
        if(onBoundStape==null) {
            super.onBackPressed()
        }
    }

}
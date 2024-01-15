package com.big9.app.ui.base

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.big9.app.ui.activity.NetworkActivity
import com.big9.app.utils.helpers.NoNetworkReceiver
import com.big9.app.utils.interfaces.NetworkCallBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity: AppCompatActivity()/*, NetworkConnectionListener */{
   lateinit var nonetwork : NoNetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*nonetwork = NoNetworkReceiver(object :NetworkConnectionListener{
            override fun onNetworkConnection(connectionStatus: Boolean) {
                *//*if (!MethodClass.check_networkconnection(this@BaseActivity)){
                    startActivity(Intent(this@BaseActivity, NetworkActivity::class.java))
                }*//*
                if(connectionStatus){
                    startActivity(Intent(this@BaseActivity, NetworkActivity::class.java))
                }

            }

        })*/

        nonetwork = NoNetworkReceiver(object : NetworkCallBack {
            override fun hasNetwork(hasnetwork: Boolean) {
                if(!hasnetwork){
                   startActivity(Intent(this@BaseActivity, NetworkActivity::class.java))

                }
            }
        })
     }

    override fun onResume() {
        super.onResume()
       registerReceiver(nonetwork, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(nonetwork)
    }


    /* override fun onNetworkConnection(connectionStatus: Boolean) {
         if (!MethodClass.check_networkconnection(this))
             startActivity(Intent(this, NetworkActivity::class.java))
     }*/



}
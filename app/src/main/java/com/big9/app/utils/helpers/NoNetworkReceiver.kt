package com.big9.app.utils.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.interfaces.NetworkCallBack


class NoNetworkReceiver(val ntwrklstnr: NetworkCallBack) : BroadcastReceiver(){
    override fun onReceive(ctx: Context?, p1: Intent?) {
        ctx?.let {

            if (!MethodClass.check_networkconnection(it)){
           // if (!MethodClass.checkNetworkConnection(it)){
                ntwrklstnr?.hasNetwork(false)

            }else{
                ntwrklstnr?.hasNetwork(true)
            }
        }

    }


}
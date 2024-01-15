package com.big9.app.utils.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class OtpRetriever : BroadcastReceiver() {
    //private var smsBroadcastReceiverListener: SmsBroadcastReceiverListener? = null
    override fun onReceive(context: Context, intent: Intent) {
        /*if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status?.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val messageIntent: Intent? =
                        extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT)
                   // smsBroadcastReceiverListener?.onSuccess(messageIntent)
                    //var message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE)
                    Log.d("TAG_otp", "onReceive: aaaa $messageIntent")
                }
                CommonStatusCodes.TIMEOUT -> {
                   // smsBroadcastReceiverListener?.onFailure()
                }
            }
        }*/
    }

   /* fun setListener(smsBroadcastReceiverListener: SmsBroadcastReceiverListener) {
        this.smsBroadcastReceiverListener = smsBroadcastReceiverListener
    }*/
}
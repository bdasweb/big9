package com.big9.app.utils.`interface`

import android.content.Intent




interface SmsBroadcastReceiverListener {
    fun onSuccess(intent: Intent?)

    fun onFailure()
}
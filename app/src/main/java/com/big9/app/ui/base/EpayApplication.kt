package com.big9.app.ui.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EpayApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
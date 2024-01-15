package com.big9.app.ui.base

import com.big9.app.di.AppModule
import com.google.android.datatransport.runtime.dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {
    // Component methods
}
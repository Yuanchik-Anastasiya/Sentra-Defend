package com.yuanchik.sentradefend.presentation.viewmodel

import android.app.Application
import com.yuanchik.sentradefend.di.AppComponent
import com.yuanchik.sentradefend.di.AppModule
import com.yuanchik.sentradefend.di.DaggerAppComponent


class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
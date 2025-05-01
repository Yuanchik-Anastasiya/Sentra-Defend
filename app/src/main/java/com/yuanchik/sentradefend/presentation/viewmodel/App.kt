package com.yuanchik.sentradefend.presentation.viewmodel

import android.app.Application
import androidx.room.Room
import com.yuanchik.sentradefend.data.AppDatabase
import com.yuanchik.sentradefend.di.AppComponent
import com.yuanchik.sentradefend.di.AppModule
import com.yuanchik.sentradefend.di.DaggerAppComponent


class App : Application() {
    lateinit var appComponent: AppComponent
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "scan_history_db"
        ).build()
    }
}

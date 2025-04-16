package com.example.fakestore

import android.app.Application
import com.example.fakestore.di.AppComponent
import com.example.fakestore.di.AppModule
import com.example.fakestore.di.DaggerAppComponent

class MyApp: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}
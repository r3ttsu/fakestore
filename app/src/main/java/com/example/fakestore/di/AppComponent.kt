package com.example.fakestore.di

import com.example.fakestore.LoginActivity
import com.example.fakestore.DashboardActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(activity: LoginActivity)
    fun inject(activity: DashboardActivity)
}
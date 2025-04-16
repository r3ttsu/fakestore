package com.example.fakestore.di

import android.app.Application
import com.example.fakestore.CartActivity
import com.example.fakestore.LoginActivity
import com.example.fakestore.DashboardActivity
import com.example.fakestore.DetailProductActivity
import com.example.fakestore.SplashActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, DatabaseModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(activity: SplashActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: DashboardActivity)
    fun inject(activity: DetailProductActivity)
    fun inject(activity: CartActivity)
}
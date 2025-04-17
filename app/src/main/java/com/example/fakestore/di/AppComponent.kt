package com.example.fakestore.di

import android.app.Application
import com.example.fakestore.feature.CartActivity
import com.example.fakestore.feature.LoginActivity
import com.example.fakestore.feature.DashboardActivity
import com.example.fakestore.feature.DetailProductActivity
import com.example.fakestore.feature.SplashActivity
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
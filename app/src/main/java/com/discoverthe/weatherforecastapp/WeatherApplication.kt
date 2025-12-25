package com.discoverthe.weatherforecastapp

import android.app.Application
import com.discoverthe.weatherforecastapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApplication)
            modules(appModule)
        }
    }
}

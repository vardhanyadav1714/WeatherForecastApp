package com.discoverthe.weatherforecastapp.di

import androidx.room.Room
import com.discoverthe.weatherforecastapp.data.WeatherDatabase
import com.discoverthe.weatherforecastapp.repositary.WeatherDbRepository
import com.discoverthe.weatherforecastapp.repositary.WeatherRepository
import com.discoverthe.weatherforecastapp.screens.favorites.FavoriteViewModel
import com.discoverthe.weatherforecastapp.screens.settings.SettingsViewModel
import com.discoverthe.weatherforecastapp.screens.splash.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            WeatherDatabase::class.java,
            "weather_database"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<WeatherDatabase>().weatherDao() }

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }

    single { WeatherRepository(get()) }
    single { WeatherDbRepository(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}

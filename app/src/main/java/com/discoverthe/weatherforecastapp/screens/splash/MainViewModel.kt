package com.discoverthe.weatherforecastapp.screens.splash

import androidx.lifecycle.ViewModel
import com.discoverthe.weatherforecastapp.data.DataOrException
import com.discoverthe.weatherforecastapp.model.WeatherResponse
import com.discoverthe.weatherforecastapp.repositary.WeatherRepository

class MainViewModel(private val repository: WeatherRepository) : ViewModel() {
    suspend fun getWeatherData(city: String): DataOrException<WeatherResponse, Boolean, Exception> {
        return repository.getWeather(cityQuery = city)
    }
}

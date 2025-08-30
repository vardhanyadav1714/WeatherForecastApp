package com.example.weatherforecastapp.screens.splash

import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.data.DataOrException
import com.example.weatherforecastapp.model.Weather
import com.example.weatherforecastapp.repositary.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository):ViewModel() {
    suspend fun getWeatherData(city: String, units: String):DataOrException<Weather,Boolean,Exception>{
        return repository.getWeather(cityQuery = city,units=units)
    }
}

package com.example.weatherforecastapp.repositary

import com.example.weatherforecastapp.data.DataOrException
import com.example.weatherforecastapp.model.Weather
import com.example.weatherforecastapp.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api:WeatherApi){
    suspend fun getWeather(cityQuery: String, units: String):DataOrException<Weather,Boolean,Exception>{
       val response = try{
           api.getWeather(query = cityQuery, units = units)
       }catch (e:Exception){
           return DataOrException(e=e)
       }
        return DataOrException(data=response)
    }
}
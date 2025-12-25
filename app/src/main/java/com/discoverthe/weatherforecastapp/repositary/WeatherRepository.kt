package com.discoverthe.weatherforecastapp.repositary

import com.discoverthe.weatherforecastapp.data.DataOrException
import com.discoverthe.weatherforecastapp.model.WeatherResponse
import com.discoverthe.weatherforecastapp.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WeatherRepository(private val client: HttpClient) {
    suspend fun getWeather(cityQuery: String): DataOrException<WeatherResponse, Boolean, Exception> {
        return try {
            val response: WeatherResponse = client.get("${Constants.BASE_URL}forecast.json") {
                parameter("key", Constants.API_KEY)
                parameter("q", cityQuery)
                parameter("days", "14")
                parameter("aqi", "yes")
                parameter("alerts", "no")
            }.body()
            DataOrException(data = response)
        } catch (e: Exception) {
            DataOrException(e = e)
        }
    }
}

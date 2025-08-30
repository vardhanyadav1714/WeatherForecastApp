package com.example.weatherforecastapp.repositary

import com.example.weatherforecastapp.data.WeatherDao
import com.example.weatherforecastapp.model.Favorite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDbRepository @Inject constructor(private val weatherDao: WeatherDao) {
    fun getFavorites(): kotlinx.coroutines.flow.Flow<List<Favorite>> = weatherDao.getFavorites()
    suspend fun insertFavorite(favorite: Favorite) = weatherDao.insertFavorite(favorite)
    suspend fun updateFavorite(favorite: Favorite) = weatherDao.updateFavorite(favorite)
    suspend fun deleteAllFavorite() = weatherDao.deleteAllFavorite()
    suspend fun deleteFavorite(favorite: Favorite)=weatherDao.deleteFavorite(favorite)
    suspend fun getFavById(city:String):Favorite=weatherDao.getFavById(city)

    fun getUnits():Flow<List<com.example.weatherforecastapp.model.Unit>> = weatherDao.getUnits()

    suspend fun insertUnit(unit:com.example.weatherforecastapp.model.Unit)=weatherDao.insertUnit(unit)
    suspend fun updateUnit(unit: com.example.weatherforecastapp.model.Unit)=weatherDao.updateUnit(unit)
    suspend fun deleteAllUnits()=weatherDao.deleteAllUnits()
    suspend fun deleteUnit(unit: com.example.weatherforecastapp.model.Unit)=weatherDao.deleteUnit(unit)
}
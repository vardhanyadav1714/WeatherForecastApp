package com.discoverthe.weatherforecastapp.repositary

import com.discoverthe.weatherforecastapp.data.WeatherDao
import com.discoverthe.weatherforecastapp.model.Favorite
import kotlinx.coroutines.flow.Flow


class WeatherDbRepository(private val weatherDao: WeatherDao) {
    fun getFavorites(): kotlinx.coroutines.flow.Flow<List<Favorite>> = weatherDao.getFavorites()
    suspend fun insertFavorite(favorite: Favorite) = weatherDao.insertFavorite(favorite)
    suspend fun updateFavorite(favorite: Favorite) = weatherDao.updateFavorite(favorite)
    suspend fun deleteAllFavorite() = weatherDao.deleteAllFavorite()
    suspend fun deleteFavorite(favorite: Favorite)=weatherDao.deleteFavorite(favorite)
    suspend fun getFavById(city:String):Favorite=weatherDao.getFavById(city)

    fun getUnits():Flow<List<com.discoverthe.weatherforecastapp.model.Unit>> = weatherDao.getUnits()

    suspend fun insertUnit(unit:com.discoverthe.weatherforecastapp.model.Unit)=weatherDao.insertUnit(unit)
    suspend fun updateUnit(unit: com.discoverthe.weatherforecastapp.model.Unit)=weatherDao.updateUnit(unit)
    suspend fun deleteAllUnits()=weatherDao.deleteAllUnits()
    suspend fun deleteUnit(unit: com.discoverthe.weatherforecastapp.model.Unit)=weatherDao.deleteUnit(unit)
}

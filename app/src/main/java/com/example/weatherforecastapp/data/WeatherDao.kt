package com.example.weatherforecastapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecastapp.model.Favorite
import kotlinx.coroutines.flow.Flow
@Dao
interface WeatherDao {

    @Query("SELECT * FROM fav_tbl")
    fun getFavorites(): Flow<List<Favorite>>

    @Query("SELECT * FROM fav_tbl where city =:city")
    suspend fun getFavById(city:String):Favorite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavorite(favorite: Favorite)

    @Query("DELETE from fav_tbl")
    suspend fun deleteAllFavorite()

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * from settings_tbl")
    fun getUnits():Flow<List<com.example.weatherforecastapp.model.Unit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unit:com.example.weatherforecastapp.model.Unit)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUnit(unit: com.example.weatherforecastapp.model.Unit)

    @Query("DELETE from settings_tbl")
    suspend fun deleteAllUnits()

    @Delete
    suspend fun deleteUnit(unit:com.example.weatherforecastapp.model.Unit)
}
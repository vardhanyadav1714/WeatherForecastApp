package com.discoverthe.weatherforecastapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.discoverthe.weatherforecastapp.model.Favorite

@Database(entities = [Favorite::class, com.discoverthe.weatherforecastapp.model.Unit::class], version = 3, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}

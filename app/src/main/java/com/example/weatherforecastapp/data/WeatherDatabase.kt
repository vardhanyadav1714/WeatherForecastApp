package com.example.weatherforecastapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherforecastapp.model.Favorite

@Database(entities = [Favorite::class, com.example.weatherforecastapp.model.Unit::class], version = 3, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}

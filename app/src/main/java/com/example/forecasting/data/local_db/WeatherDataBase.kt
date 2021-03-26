package com.example.forecasting.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.forecasting.data.local_db.converters.*
import com.example.forecasting.data.local_db.dao.CurrentWeatherDao
import com.example.forecasting.data.local_db.dao.FavouriteDao
import com.example.forecasting.data.local_db.dao.FutureWeatherDao
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.Day
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import com.example.forecasting.data.local_db.entity.FutureWeatherResponse
import com.example.forecasting.ui.alarm.AlarmDao
import com.example.forecasting.ui.alarm.AlarmEntity

const val DATABASE_NAME = "weather_database"

@Database(entities = [CurrentweatherResponse::class,FavouriteWeatherResponse::class,AlarmEntity::class], version = 1)
@TypeConverters(WeatherDayStringConverter::class,WeatherStringConverter::class, LocalDateConverter::class,HourStringConverter::class,DayStringConverter::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    //abstract fun futureWeatherDao(): FutureWeatherDao
    abstract fun favouriteDao(): FavouriteDao
    abstract fun alarmDao():AlarmDao
    companion object {
        @Volatile//to be seen by all the threads
        //singleton
        private var instance: WeatherDataBase? = null
        private val LOCK = Any()//to prevent two threads from doing the same thing at the same time

        //instead getDataBaseInstance()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDataBase(context).also { instance = it }
        }

    fun buildDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext, WeatherDataBase::class.java,
            DATABASE_NAME
        )
        .allowMainThreadQueries()
        .build()
    }
}
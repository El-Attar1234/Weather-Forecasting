package com.example.forecasting.data.local_db.local_data_source

import com.example.forecasting.data.local_db.dao.AlarmDao
import com.example.forecasting.data.local_db.dao.CurrentWeatherDao
import com.example.forecasting.data.local_db.dao.FavouriteDao
import com.example.forecasting.data.local_db.db.WeatherDataBase

class LocalDataSourceImpl(private val db:WeatherDataBase) : LocalDataSource {
    override fun alarmDao(): AlarmDao {
       return db.alarmDao()
    }

    override fun favDao(): FavouriteDao {
        return db.favouriteDao()
    }

    override fun currentDao(): CurrentWeatherDao {
       return db.currentWeatherDao()
    }
}
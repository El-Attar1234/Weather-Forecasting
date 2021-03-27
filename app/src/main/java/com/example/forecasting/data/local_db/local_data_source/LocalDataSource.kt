package com.example.forecasting.data.local_db.local_data_source

import com.example.forecasting.data.local_db.dao.AlarmDao
import com.example.forecasting.data.local_db.dao.CurrentWeatherDao
import com.example.forecasting.data.local_db.dao.FavouriteDao

interface LocalDataSource {
    fun alarmDao():AlarmDao;
    fun favDao():FavouriteDao;
    fun currentDao():CurrentWeatherDao;
}
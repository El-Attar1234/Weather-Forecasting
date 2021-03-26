package com.example.forecasting.data.local_db.dao

import com.example.forecasting.data.local_db.entity.CurrentweatherResponse

interface GenericCurrentDao<T> {
    fun insertCurrentWeather(t: T)
}
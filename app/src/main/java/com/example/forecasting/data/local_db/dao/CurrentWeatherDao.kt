package com.example.forecasting.data.local_db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecasting.data.local_db.entity.CURRENT_WEATHER_ID
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse


@Dao
interface CurrentWeatherDao:GenericCurrentDao<CurrentweatherResponse> {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertCurrentWeather(currentWeatherResponse:CurrentweatherResponse)

    @Query("Select * from current_weather ")
    fun getCurrentWeatherResponse():LiveData<CurrentweatherResponse>

    @Query("Select * from current_weather ")
    fun getCurrentWeatherResponseWithoutLiveData():CurrentweatherResponse
}
package com.example.forecasting.data.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.Day
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import com.example.forecasting.data.local_db.entity.FutureWeatherResponse
import com.example.forecasting.ui.alarm.AlarmEntity

interface WeatherRepository {
    //current
    suspend fun getCurrentWeather():LiveData<CurrentweatherResponse>
    fun getCurrentWeatherResponseWithoutLiveData():CurrentweatherResponse
    //alarm
    suspend fun getAllAlarms():List<AlarmEntity>
    suspend fun insertAlarm(alarmEntity: AlarmEntity):Long
     fun deleteAlarm(id:Int)
    fun getAlarmById(id:Int):AlarmEntity

 //favourite
    fun getAllFavouritesWithoutLiveData(): List<FavouriteWeatherResponse>
    fun deleteFavouriteLocation(id:String)
    suspend fun getFavouiteResponses():LiveData<List<FavouriteWeatherResponse>>
     fun getFavouiteLocation(id:String):FavouriteWeatherResponse

}
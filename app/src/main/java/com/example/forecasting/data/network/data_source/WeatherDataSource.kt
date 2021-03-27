package com.example.forecasting.data.network.data_source

import androidx.lifecycle.LiveData
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.Day
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse


interface WeatherDataSource {
    val downloadedCurrentWeather:LiveData<CurrentweatherResponse>
    suspend fun fetchCurrentWeather(lat:String,lon:String,ex:String,language:String)

    val downloadedFavouriteWeather:LiveData<FavouriteWeatherResponse>
    suspend fun fetchFavouriteWeather(lat:String,lon:String,ex:String)

}
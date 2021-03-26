package com.example.forecasting.data.network.data_source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.Day
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import com.example.forecasting.data.local_db.entity.FutureWeatherResponse
import com.example.forecasting.data.network.OpenWeatherApiService
import com.example.forecasting.utilities.NoConnectionException

class WeatherDataSourceImpl(private val openWeatherApiService: OpenWeatherApiService) :
    WeatherDataSource {
//is a backing property for downloaded liveData
    private val myDownloadedCurrentWeather = MutableLiveData<CurrentweatherResponse>()

    override val downloadedCurrentWeather: LiveData<CurrentweatherResponse>
        get() = myDownloadedCurrentWeather

    override suspend fun fetchCurrentWeather(lat: String, lon: String, ex: String,language:String) {
        try {
            Log.i("futureLanguage", "${language}")
            val currentResponse = openWeatherApiService.getForecast(lat, lon, ex,language)
            val response=currentResponse.await()
            Log.i("future", "inside data source , my return ${response}")
            myDownloadedCurrentWeather.postValue(response)
           // Log.i("retrofit", "after update inside data source , my return ${currentResponse.await()}")
        } catch (e: NoConnectionException) {
            Log.i("error", ""+e)
            Log.i("error", "No internet")
        }
    }

    private val myDownloadedFutureWeather = MutableLiveData<List<Day>>()

    override val downloadedFutureWeather: LiveData<List<Day>>
        get() =myDownloadedFutureWeather

    override suspend fun fetchFutureWeather(lat: String, lon: String, ex: String) {
        try {
            Log.i("retrofit", "inside data source for future")
            val futureResponse = openWeatherApiService.getFutureForecast(lat, lon, ex)
            // Log.i("retrofit", "inside data source , my return ${currentResponse.await()}")
            val futureData=futureResponse.await()
            val list=futureData.daily
            myDownloadedFutureWeather.postValue(list)
            // Log.i("retrofit", "after update inside data source , my return ${currentResponse.await()}")
        } catch (e: NoConnectionException) {
            Log.i("error", ""+e.message)
            Log.i("error", "No internet")
        }
    }
    private val myDownloadedFavouriteWeather = MutableLiveData<FavouriteWeatherResponse>()
    override val downloadedFavouriteWeather: LiveData<FavouriteWeatherResponse>
        get() = myDownloadedFavouriteWeather

    override suspend fun fetchFavouriteWeather(lat: String, lon: String, ex: String) {
        try {
            Log.i("favourite1", "inside data source for favourite")
            val futureResponse = openWeatherApiService.getFavouriteForecast(lat, lon, ex)
            // Log.i("retrofit", "inside data source , my return ${currentResponse.await()}")
            val favouriteData=futureResponse.await()
            myDownloadedFavouriteWeather.postValue(favouriteData)
             Log.i("favourite1", "after update inside data source , my return ${favouriteData.lat} , ${favouriteData.lon}")
        } catch (e: NoConnectionException) {
            Log.i("error", ""+e.message)
            Log.i("error", "No internet")
        }
    }


}
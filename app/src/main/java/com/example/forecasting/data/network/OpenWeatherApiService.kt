package com.example.forecasting.data.network


import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import com.example.forecasting.data.local_db.entity.FutureWeatherResponse
import com.example.forecasting.data.network.connection_interceptor.ConnectivityInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY="200980096369d17c8abf03be3985334c"

interface OpenWeatherApiService {

    @GET("onecall")
    fun getForecast(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("exclude") ex:String,
        @Query("lang") lang:String,
        @Query("units") units:String="standard"


    ): Deferred<CurrentweatherResponse>

    @GET("onecall")
    fun getFutureForecast(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("exclude") ex:String

    ): Deferred<FutureWeatherResponse>

    @GET("onecall")
    fun getFavouriteForecast(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("exclude") ex:String,
        @Query("lang") lang:String="ar",
        @Query("units") units:String="standard"

    ): Deferred<FavouriteWeatherResponse>





    companion object {
        operator fun invoke(
  connectivityInterceptor: ConnectivityInterceptor
        ): OpenWeatherApiService {
            //as api key passed through each request
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())//as we used our return as Deffered
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApiService::class.java)
        }
    }
}
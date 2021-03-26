package com.example.forecasting.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitFactory {
    const val BASE_URL ="https://api.openweathermap.org/data/2.5/"

    fun getRetrofit(): Retrofit {
        val myRetrofit = Retrofit.Builder().baseUrl(BASE_URL).
        addConverterFactory(GsonConverterFactory.create()).build()
        return myRetrofit
    }
}
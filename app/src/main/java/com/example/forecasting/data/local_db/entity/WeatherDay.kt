package com.example.forecasting.data.local_db.entity

import com.google.gson.annotations.SerializedName

data class WeatherDay(
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)
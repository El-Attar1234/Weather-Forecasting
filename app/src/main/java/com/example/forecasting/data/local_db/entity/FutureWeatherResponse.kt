package com.example.forecasting.data.local_db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey



data class FutureWeatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset:Int,
    val daily:ArrayList<Day>

){


}


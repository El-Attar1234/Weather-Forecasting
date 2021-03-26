package com.example.forecasting.data.local_db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Day(
    val dt: Long,
    val sunrise:Long,
    val sunset:Long,
    @Embedded(prefix = "temp_")
    val temp:Temperature,
    val pressure:Int,
    val humidity:Int,
    val wind_speed:Double,
    val visibility:Int,
    val weather:ArrayList<WeatherDay>
){
    data class Temperature(
        val day:Double,
        val min:Double
    )

}
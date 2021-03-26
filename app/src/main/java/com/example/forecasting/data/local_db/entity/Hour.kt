package com.example.forecasting.data.local_db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Hour(
    val dt: Long,
    val temp:Double,
    val feels_like:Double,
    val weather:ArrayList<Weather>
)
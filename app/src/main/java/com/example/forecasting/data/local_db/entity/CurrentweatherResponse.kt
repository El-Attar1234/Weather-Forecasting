package com.example.forecasting.data.local_db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime


const val  CURRENT_WEATHER_ID=1

@Entity(tableName = "current_weather")
data class CurrentweatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @Embedded(prefix = "current_")
    val current: Current,
    val daily:ArrayList<Day>,
    val hourly:ArrayList<Hour>

) {
    data class Current(
        val dt: Long,
        val temp: Double,
        val humidity:Int,
        val visibility:Int,
        val wind_speed:Double,
        val pressure: Int,
        val weather:ArrayList<Weather>


    )
    @PrimaryKey(autoGenerate = false)
    var  current_weather_id= CURRENT_WEATHER_ID
    val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(this.current.dt)
            val zoneId = ZoneId.of(this.timezone)
            return ZonedDateTime.ofInstant(instant, zoneId)
        }
}
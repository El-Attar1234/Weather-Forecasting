package com.example.forecasting.data.local_db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime


@Entity(tableName = "favourite_table")
data class FavouriteWeatherResponse(
    @PrimaryKey
    var cityID:String,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    @Embedded(prefix = "current_")
    val current: Current,
    val daily: ArrayList<Day>,
    val hourly: ArrayList<Hour>
) {
    data class Current(
        val dt: Long,
        val sunrise: Int,
        val sunset: Int,
        val temp: Double,
        val wind_speed: Double,
        val humidity: Int,
        val visibility: Int,
        val feels_like: Double,
        val pressure: Int,
        val weather: ArrayList<Weather>
    )
    val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(this.current.dt)
            val zoneId = ZoneId.of(this.timezone)
            return ZonedDateTime.ofInstant(instant, zoneId)
        }
}
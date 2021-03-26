package com.example.forecasting.data.local_db.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.Day
import com.example.forecasting.data.local_db.entity.FutureWeatherResponse


@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFutureWeather(day: Day)

    @Query("Select * from future_weather where dt>:l")
    fun getFutureWeatherResponseWithoutLiveData(l:Long): LiveData<List<Day>>

    @Query("Select * from future_weather where dt==:l")
    fun getSpecificFutureWeather(l:Long): LiveData<Day>


    @Query("select count(id) from future_weather where dt>:l")
    fun countFutureWeather(l:Long): Int

    @Query("delete from future_weather")
    fun deleteOldEntries()
}


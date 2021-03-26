package com.example.forecasting.data.local_db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse


@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteWeather(favouriteWeatherResponse: FavouriteWeatherResponse)

    @Query("Select * from favourite_table ")
    fun getAllFavourites(): LiveData<List<FavouriteWeatherResponse>>

    @Query("Select * from favourite_table ")
    fun getAllFavouritesWithoutLiveData(): List<FavouriteWeatherResponse>

    @Query("delete from favourite_table where cityID=:id")
    fun deleteFavouriteLocation(id:String)

    @Query("select * from favourite_table where cityID=:id")
    fun getFavouriteLocation(id:String):FavouriteWeatherResponse
}
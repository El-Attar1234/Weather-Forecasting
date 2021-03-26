package com.example.forecasting.ui.favourite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import com.example.forecasting.data.repository.WeatherRepository
import com.example.forecasting.utilities.Event
import com.example.forecasting.utilities.lazyDeferred


class FavouriteViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private var mutableFavouriteDelete= MutableLiveData<Event<String>>()
    private var mutableFavouriteDetail= MutableLiveData<Event<String>>()


    val fetchFavouriteWeather by lazyDeferred {
        Log.i("favourite1", ": inside view model to get from data base")
        return@lazyDeferred weatherRepository.getFavouiteResponses()}


    fun pressOnDeleteIcon(id:String){
        mutableFavouriteDelete.value= Event(id)
    }

    fun showDetails(id:String){
        mutableFavouriteDetail.value= Event(id)
    }

    val favouriteDeletedId: LiveData<Event<String>>
        get() = mutableFavouriteDelete

    val favouriteDetailedId: LiveData<Event<String>>
        get() = mutableFavouriteDetail


   /* fun getAllFavouritesWithoutLiveData(): List<FavouriteWeatherResponse>{

    }*/
    fun deleteFavouriteLocation(id:String){
       weatherRepository.deleteFavouriteLocation(id)
   }
    fun saveFavouriteInDataBase(lat: String, long: String) {
     //   weatherRepository.

    }

}
package com.example.forecasting.ui.weather.weak_forecast.day_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forecasting.data.local_db.entity.Day
import com.example.forecasting.data.provider.UnitProvider
import com.example.forecasting.data.repository.WeatherRepository
//import com.example.forecasting.utilities.UnitSystem
import com.example.forecasting.utilities.lazyDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FutureListViewModel(
    private val weatherRepository: WeatherRepository,
                          unitProvider: UnitProvider
) : ViewModel() {
   /* private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
        get() = unitSystem == UnitSystem.METRIC*/
    private val mySpecidicWeather = MutableLiveData<Day>()

    val specidicWeather: LiveData<Day>
        get() =mySpecidicWeather


    val fetchCurrentWeather by lazyDeferred {
        Log.i("retrofit", ": inside view model to get from data base")
        return@lazyDeferred weatherRepository.getCurrentWeather()}

  /*  val fetchSpecificWeather by lazyDeferred {
        Log.i("retrofit", ": inside view model to get from data base")
        val specifcWeather=weatherRepository.getCurrentWeather()
        mySpecidicWeather.value=specidicWeather.value
        return@lazyDeferred specifcWeather}*/

    fun fetchSpecificWeather(dt:Long) {
        GlobalScope.launch {
            Log.i("future", ":inside coroutine ")
      //  val specifcWeather=weatherRepository.getSpecificFutureWeather(dt)
        //    Log.i("future", ":inside coroutine ${specifcWeather.value?.dt}")
            ///   mySpecidicWeather.value=specidicWeather.value
    }}
}



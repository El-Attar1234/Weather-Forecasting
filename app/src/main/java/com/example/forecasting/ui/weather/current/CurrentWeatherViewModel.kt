package com.example.forecasting.ui.weather.current

import  android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forecasting.domain.use_case.provider.unit.UnitProvider
import com.example.forecasting.domain.repo.WeatherRepository
import com.example.forecasting.utilities.Event
//import com.example.forecasting.utilities.UnitSystem
import com.example.forecasting.utilities.lazyDeferred

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val navMutablLiveData = MutableLiveData<Event<Int>>()

    fun willNavigate(position: Int) {
        navMutablLiveData.value = Event(position)
    }

    val getNavigation: LiveData<Event<Int>>
        get() = navMutablLiveData

    //private val unitSystem = unitProvider.getUnitSystem()
    val tempUnitSystem = unitProvider.getTempUnitSystem()
    val windUnitSystem = unitProvider.getWindUnitSystem()

    /* val isMetricUnit: Boolean
         get() = unitSystem == UnitSystem.METRIC*/

    val fetchCurrentWeather by lazyDeferred {
        Log.i("debug", ": inside view model to get from data base")
        Log.i("debug", ": in view model ## ${weatherRepository.getCurrentWeather()}")
        return@lazyDeferred weatherRepository.getCurrentWeather()
    }


}
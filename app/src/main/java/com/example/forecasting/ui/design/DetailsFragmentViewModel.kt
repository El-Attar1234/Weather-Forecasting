package com.example.forecasting.ui.design

import androidx.lifecycle.ViewModel
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import com.example.forecasting.data.provider.UnitProvider
import com.example.forecasting.data.repository.WeatherRepository

class DetailsFragmentViewModel
    (private val weatherRepository: WeatherRepository,
                               unitProvider: UnitProvider
): ViewModel() {

    val tempUnitSystem = unitProvider.getTempUnitSystem()
    val windUnitSystem = unitProvider.getWindUnitSystem()

    fun getDetailsCurrentDay():CurrentweatherResponse{
       return weatherRepository.getCurrentWeatherResponseWithoutLiveData()
    }
    fun getFavouriteLocation(id:String): FavouriteWeatherResponse {
        return weatherRepository.getFavouiteLocation(id)
    }

}
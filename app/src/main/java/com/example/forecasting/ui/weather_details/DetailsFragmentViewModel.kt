package com.example.forecasting.ui.weather_details

import androidx.lifecycle.ViewModel
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import com.example.forecasting.domain.use_case.provider.unit.UnitProvider
import com.example.forecasting.domain.repo.WeatherRepository

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
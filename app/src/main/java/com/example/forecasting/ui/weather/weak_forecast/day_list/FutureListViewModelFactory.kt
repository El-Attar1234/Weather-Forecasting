package com.example.forecasting.ui.weather.weak_forecast.day_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecasting.data.provider.UnitProvider
import com.example.forecasting.data.repository.WeatherRepository
import com.example.forecasting.ui.weather.current.CurrentWeatherViewModel

class FutureListViewModelFactory (
    private val weatherRepository: WeatherRepository,
    private val unitProvider: UnitProvider

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureListViewModel(weatherRepository, unitProvider) as T
    }
}
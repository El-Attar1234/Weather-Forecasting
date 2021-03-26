package com.example.forecasting.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecasting.data.provider.UnitProvider
import com.example.forecasting.data.repository.WeatherRepository


class CurrentWeatherViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val unitProvider: UnitProvider

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository, unitProvider) as T
    }
}
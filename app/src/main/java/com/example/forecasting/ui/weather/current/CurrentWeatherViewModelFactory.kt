package com.example.forecasting.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecasting.domain.use_case.provider.unit.UnitProvider
import com.example.forecasting.domain.repo.WeatherRepository


class CurrentWeatherViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val unitProvider: UnitProvider

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository, unitProvider) as T
    }
}
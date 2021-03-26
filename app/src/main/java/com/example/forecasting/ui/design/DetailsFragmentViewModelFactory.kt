package com.example.forecasting.ui.design

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecasting.data.provider.UnitProvider
import com.example.forecasting.data.repository.WeatherRepository
import com.example.forecasting.ui.alarm.alarm_seetings.AlarmSettingsViewModel

class DetailsFragmentViewModelFactory (
    private val weatherRepository: WeatherRepository,
    private val unitProvider: UnitProvider

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsFragmentViewModel(weatherRepository,unitProvider) as T
    }
}
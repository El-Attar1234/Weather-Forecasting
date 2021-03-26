package com.example.forecasting.ui.alarm.alarm_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecasting.data.repository.WeatherRepository

class AlarmViewModelFactory(
    private val weatherRepository: WeatherRepository

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmViewModel(weatherRepository) as T
    }
}
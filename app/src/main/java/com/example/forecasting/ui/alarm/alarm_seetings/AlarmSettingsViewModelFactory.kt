package com.example.forecasting.ui.alarm.alarm_seetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecasting.domain.repo.WeatherRepository

class AlarmSettingsViewModelFactory(
    private val weatherRepository: WeatherRepository

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmSettingsViewModel(weatherRepository) as T
    }
}
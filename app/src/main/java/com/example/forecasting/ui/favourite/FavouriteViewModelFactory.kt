package com.example.forecasting.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecasting.data.repository.WeatherRepository

class FavouriteViewModelFactory(
    private val weatherRepository: WeatherRepository

) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouriteViewModel(weatherRepository) as T
    }
}
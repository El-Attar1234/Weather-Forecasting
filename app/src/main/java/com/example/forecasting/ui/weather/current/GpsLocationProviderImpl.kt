package com.example.forecasting.ui.weather.current

import android.content.ContentProvider
import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.preference.PreferenceManager
import com.example.forecasting.domain.use_case.provider.location.USE_DEVICE_LOCATION
import com.google.android.gms.location.FusedLocationProviderClient

class GpsLocationProviderImpl(  private val fusedLocationClient: FusedLocationProviderClient,private val provider: ContentProvider,private val context: Context) : GpsLocationProvider{
   val appContext=context.applicationContext

    override fun getLastLocation() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext(provider))
        if (preferences.getBoolean(USE_DEVICE_LOCATION, true)) {
            //getLastLlocation()
        }
    }



}
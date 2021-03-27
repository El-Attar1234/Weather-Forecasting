package com.example.forecasting.ui.weather.current

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.preference.PreferenceManager
import com.example.forecasting.data.provider.USE_DEVICE_LOCATION
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.IOException

class GpsLocationProviderImpl(  private val fusedLocationClient: FusedLocationProviderClient,private val provider: ContentProvider,private val context: Context) : GpsLocationProvider{
   val appContext=context.applicationContext

    override fun getLastLocation() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext(provider))
        if (preferences.getBoolean(USE_DEVICE_LOCATION, true)) {
            //getLastLlocation()
        }
    }



}
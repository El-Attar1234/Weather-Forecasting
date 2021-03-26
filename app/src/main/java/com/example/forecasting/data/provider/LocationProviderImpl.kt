package com.example.forecasting.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat

import androidx.preference.PreferenceManager
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.provider.enums.LanguageUnitSystem
import com.example.forecasting.data.provider.enums.TempUnitSystem
import com.example.forecasting.data.provider.enums.WindUnitSystem
import com.example.forecasting.utilities.LocationPermissionNotGrantedException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred


const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"
const val LANGUAGE_UNIT_SYSTEM="LANGUAGE_UNIT_SYSTEM"
class LocationProviderImpl(
    context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationProvider {
    private val appContext = context.applicationContext
    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    override suspend fun getLatitude(): String {
        val sharedPref = appContext.getSharedPreferences("location", Context.MODE_PRIVATE)
        Log.i("fetch", "getLatitude: " + sharedPref.getString("MYLAT", "30")!!)
        return sharedPref.getString("MYLAT", "30.7114093")!!
    }

    override suspend fun getLong(): String {
        val sharedPref = appContext.getSharedPreferences("location", Context.MODE_PRIVATE)
        return sharedPref.getString("MYLONG", "31.6106543")!!
    }

    override suspend fun getFavLatitude(): String {
        val sharedPref = appContext.getSharedPreferences("favourite", Context.MODE_PRIVATE)
        val lat=sharedPref.getString("MYLAT_FAV", "-1")!!
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("MYLAT_FAV","-1")
        return lat
    }

    override suspend fun getFavLong(): String {
        val sharedPref = appContext.getSharedPreferences("favourite", Context.MODE_PRIVATE)
        val lon=sharedPref.getString("MYLONG_FAV", "-1")!!

        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("MYLONG_FAV","-1")
        editor.apply()
        return lon
    }

    override suspend fun getLanguage(): String {
        val sharedPref = appContext.getSharedPreferences("current_language", Context.MODE_PRIVATE)
        val selectedTempUnit= preferences.getString(LANGUAGE_UNIT_SYSTEM, LanguageUnitSystem.ARABIC.name)
        val value=LanguageUnitSystem.valueOf(selectedTempUnit!!)

        var abbreviation = "ar"
        val editor: SharedPreferences.Editor = sharedPref.edit()


        if (value ==LanguageUnitSystem.ENGLISH) {
            abbreviation = "en"
            editor.putInt("MYCURRENTLANGUAGE",0)
            editor.apply()
        }else{
            editor.putInt("MYCURRENTLANGUAGE",1)
            editor.apply()
        }
        return abbreviation
    }

    override suspend fun hasLanguageChanged(): Boolean {
        var hasChanged=false
        val sharedPref = appContext.getSharedPreferences("current_language", Context.MODE_PRIVATE)
        val currentValue=sharedPref.getInt("MYCURRENTLANGUAGE",-1)
        val selectedTempUnit= preferences.getString(LANGUAGE_UNIT_SYSTEM, LanguageUnitSystem.ARABIC.name)
        val value=LanguageUnitSystem.valueOf(selectedTempUnit!!)
        if (value ==LanguageUnitSystem.ENGLISH) {
            hasChanged= currentValue==1
        }
      return hasChanged
    }


    override suspend fun hasLocationChanged(lastWeatherLocation: CurrentweatherResponse): Boolean {
        return (try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        } || hasCustomLocationChanged(lastWeatherLocation))

    }


    //in api there is a overloaded method one for lon and lat and another for city name
    //#$# is used for splitting
    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude}#$#${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGrantedException) {
                return "${getCustomLocationName()}"
            }
        } else
            return "${getCustomLocationName()}"
    }


    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: CurrentweatherResponse): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        // Comparing doubles cannot be done with "=="
        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonThreshold
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun hasCustomLocationChanged(lastWeatherLocation: CurrentweatherResponse): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastWeatherLocation.timezone
        }
        return false
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }


}


//convert task to deffered
fun <T> Task<T>.asDeferred(): Deferred<T> {
    val deferred = CompletableDeferred<T>()

    this.addOnSuccessListener { result ->
        deferred.complete(result)
    }

    this.addOnFailureListener { exception ->
        deferred.completeExceptionally(exception)
    }

    return deferred
}
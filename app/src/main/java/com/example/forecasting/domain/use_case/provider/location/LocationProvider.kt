package com.example.forecasting.domain.use_case.provider.location

import com.example.forecasting.data.local_db.entity.CurrentweatherResponse

interface LocationProvider {
     //use custom return form data base later
    suspend fun hasLocationChanged(lastWeatherLocation: CurrentweatherResponse): Boolean
    suspend fun getPreferredLocationString(): String
    suspend fun getLatitude():String
    suspend fun getLong():String
    suspend fun getFavLatitude():String
    suspend fun getFavLong():String
    suspend fun getLanguage():String
    suspend fun hasLanguageChanged():Boolean

}
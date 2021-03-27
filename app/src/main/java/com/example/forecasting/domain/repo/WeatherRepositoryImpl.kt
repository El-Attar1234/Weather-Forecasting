package com.example.forecasting.domain.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecasting.data.local_db.dao.CurrentWeatherDao
import com.example.forecasting.data.local_db.dao.FavouriteDao
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import com.example.forecasting.data.network.data_source.WeatherDataSource
import com.example.forecasting.domain.use_case.provider.location.LocationProvider
import com.example.forecasting.data.local_db.dao.AlarmDao
import com.example.forecasting.data.local_db.entity.AlarmEntity
import com.example.forecasting.data.local_db.local_data_source.LocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class WeatherRepositoryImpl(
    private val context: Context,
    private val weatherDataSource: WeatherDataSource,
    private val localDataSource: LocalDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {
    val appContext = context.applicationContext
    private lateinit var address: String

    init {
        weatherDataSource.downloadedCurrentWeather.observeForever { observer ->
            GlobalScope.launch(Dispatchers.IO) {
                localDataSource.currentDao().insertCurrentWeather(observer)
            }

        }
        weatherDataSource.downloadedFavouriteWeather.observeForever { observer ->
            GlobalScope.launch(Dispatchers.IO) {
                observer.cityID = "${observer.lat}=#=${observer.lon}"
                val myfavObject: FavouriteWeatherResponse = observer
                if (observer is FavouriteWeatherResponse) {
                    localDataSource.favDao().insertFavouriteWeather(observer)
                }
            }

        }

    }


    override suspend fun getFavouiteResponses(): LiveData<List<FavouriteWeatherResponse>> {
        return withContext(Dispatchers.IO) {
            val lat = locationProvider.getFavLatitude()
            val long = locationProvider.getFavLong()
            if (isNetworkAvailable(appContext)) {
                if (lat == (-1).toString() || long == (-1).toString()) {
                } else {
                    weatherDataSource.fetchFavouriteWeather(
                        lat,
                        long,
                        "minutely"
                    )
                }
            }

            return@withContext localDataSource.favDao().getAllFavourites()
        }

    }

    override fun getFavouiteLocation(id: String): FavouriteWeatherResponse {
        return localDataSource.favDao().getFavouriteLocation(id)
    }


    override suspend fun getCurrentWeather(): LiveData<CurrentweatherResponse> {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable(appContext)) {
                firstNeedForCurrentweatherData()
            }
            return@withContext localDataSource.currentDao().getCurrentWeatherResponse()
        }

    }

    ///here managing the settings and decide to call api or not
    private suspend fun firstNeedForCurrentweatherData() {

        val myObjectInDataBase =
            localDataSource.currentDao().getCurrentWeatherResponseWithoutLiveData()
        if (myObjectInDataBase == null || locationProvider.hasLocationChanged(myObjectInDataBase) ||
            isFetchCurrentNeeded(myObjectInDataBase.zonedDateTime) || locationProvider.hasLanguageChanged()
        ) {
            val language = locationProvider.getLanguage()
            weatherDataSource.fetchCurrentWeather(
                locationProvider.getLatitude(),
                locationProvider.getLong(),
                "minutely",
                language
            )
            return
        }


    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }


    override suspend fun getAllAlarms(): List<AlarmEntity> {
        return localDataSource.alarmDao().getAllAlarms()
    }

    override suspend fun insertAlarm(alarmEntity: AlarmEntity): Long {
        return localDataSource.alarmDao().insertAlarm(alarmEntity)
    }

    override fun deleteAlarm(id: Int) {
        localDataSource.alarmDao().deleteAlarm(id)
    }

    override fun getCurrentWeatherResponseWithoutLiveData(): CurrentweatherResponse {
        return localDataSource.currentDao().getCurrentWeatherResponseWithoutLiveData()
    }

    override fun getAlarmById(id: Int): AlarmEntity {
        return localDataSource.alarmDao().getAlarmById(id)
    }

    override fun getAllFavouritesWithoutLiveData(): List<FavouriteWeatherResponse> {
        return localDataSource.favDao().getAllFavouritesWithoutLiveData()
    }

    override fun deleteFavouriteLocation(id: String) {
        localDataSource.favDao().deleteFavouriteLocation(id)
    }

}


fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}
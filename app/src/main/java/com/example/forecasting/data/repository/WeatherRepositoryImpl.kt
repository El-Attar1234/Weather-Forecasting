package com.example.forecasting.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.forecasting.data.local_db.dao.CurrentWeatherDao
import com.example.forecasting.data.local_db.dao.FavouriteDao
import com.example.forecasting.data.local_db.dao.FutureWeatherDao
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.Day
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import com.example.forecasting.data.local_db.entity.FutureWeatherResponse
import com.example.forecasting.data.network.data_source.WeatherDataSource
import com.example.forecasting.data.provider.LocationProvider
import com.example.forecasting.ui.alarm.AlarmDao
import com.example.forecasting.ui.alarm.AlarmEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.io.IOException

class WeatherRepositoryImpl(
    private val context: Context,
    private val weatherDataSource: WeatherDataSource,
    private val alarmDao: AlarmDao,
    private val currentWeatherDao: CurrentWeatherDao,
    private val favouriteDao: FavouriteDao,
    private val locationProvider: LocationProvider
) : WeatherRepository {
    val appContext = context.applicationContext
    private lateinit var address: String

    init {
        weatherDataSource.downloadedCurrentWeather.observeForever { observer ->
            GlobalScope.launch(Dispatchers.IO) {
                Log.i("debug", "inside observer data source$observer")
                currentWeatherDao.insertCurrentWeather(observer)
                Log.i("debug", "after insert")
            }

        }
        weatherDataSource.downloadedFavouriteWeather.observeForever { observer ->
            GlobalScope.launch(Dispatchers.IO) {
                Log.i("favourite1", "after observing from api")
                Log.i("favourite1", "before insert")
                observer.cityID = "${observer.lat}=#=${observer.lon}"
                Log.i("favourite1", "prepare cityId ${observer.cityID}")
                  val myfavObject:FavouriteWeatherResponse=observer
                if (observer is FavouriteWeatherResponse) {
                    Log.i("favourite1", "after insert1")
                    favouriteDao.insertFavouriteWeather(observer)
                    Log.i("favourite1", "after insert2")
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

                    Log.i("favourite1", "inside repo -1")
                } else {
                    Log.i("favourite1", "inside repo not -1")
                    weatherDataSource.fetchFavouriteWeather(
                        lat,
                        long,
                        "minutely"
                    )
                }
            }

            Log.i("favourite1", "inside repo after fetch from api")
            return@withContext favouriteDao.getAllFavourites()
        }

    }

    override fun getFavouiteLocation(id: String): FavouriteWeatherResponse {
        return favouriteDao.getFavouriteLocation(id)
    }


    override suspend fun getCurrentWeather(): LiveData<CurrentweatherResponse> {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable(appContext)) {
                firstNeedForCurrentweatherData()
            }
            Log.i("debug", ": inside repo" + currentWeatherDao.getCurrentWeatherResponse().value)
            return@withContext currentWeatherDao.getCurrentWeatherResponse()
        }

    }

    ///here managing the settings and decide to call api or not
    private suspend fun firstNeedForCurrentweatherData() {

        val myObjectInDataBase = currentWeatherDao.getCurrentWeatherResponseWithoutLiveData()
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
        return alarmDao.getAllAlarms()
    }

    override suspend fun insertAlarm(alarmEntity: AlarmEntity): Long {
        return alarmDao.insertAlarm(alarmEntity)
    }

    override fun deleteAlarm(id: Int) {
        alarmDao.deleteAlarm(id)
    }

    override fun getCurrentWeatherResponseWithoutLiveData(): CurrentweatherResponse {
        return currentWeatherDao.getCurrentWeatherResponseWithoutLiveData()
    }

    override fun getAlarmById(id: Int): AlarmEntity {
        return alarmDao.getAlarmById(id)
    }

    override fun getAllFavouritesWithoutLiveData(): List<FavouriteWeatherResponse> {
        return favouriteDao.getAllFavouritesWithoutLiveData()
    }

    override fun deleteFavouriteLocation(id: String) {
        favouriteDao.deleteFavouriteLocation(id)
    }

}


fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}
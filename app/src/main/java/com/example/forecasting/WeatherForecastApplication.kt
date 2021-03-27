package com.example.forecasting

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.example.forecasting.data.local_db.WeatherDataBase
import com.example.forecasting.data.network.connection_interceptor.ConnectivityInterceptor
import com.example.forecasting.data.network.connection_interceptor.ConnectivityInterceptorImpl
import com.example.forecasting.data.network.OpenWeatherApiService
import com.example.forecasting.data.network.data_source.WeatherDataSource
import com.example.forecasting.data.network.data_source.WeatherDataSourceImpl
import com.example.forecasting.data.provider.LocationProvider
import com.example.forecasting.data.provider.LocationProviderImpl
import com.example.forecasting.data.provider.UnitProvider
import com.example.forecasting.data.provider.UnitProviderImpl
import com.example.forecasting.data.repository.WeatherRepository
import com.example.forecasting.data.repository.WeatherRepositoryImpl
import com.example.forecasting.ui.alarm.alarm_list.AlarmViewModelFactory
import com.example.forecasting.ui.alarm.alarm_seetings.AlarmSettingsViewModelFactory
import com.example.forecasting.ui.design.DetailsFragmentViewModelFactory
import com.example.forecasting.ui.favourite.FavouriteViewModelFactory
import com.example.forecasting.ui.weather.current.CurrentWeatherViewModelFactory
import com.example.forecasting.ui.weather.current.GpsLocationProvider
import com.example.forecasting.ui.weather.current.GpsLocationProviderImpl
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeatherForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        //inject things such as context and different services and any thing related to android
        import(androidXModule(this@WeatherForecastApplication))
        //due to type inference context will be passed
        bind() from singleton { WeatherDataBase(instance()) }
        bind() from singleton { instance<WeatherDataBase>().currentWeatherDao() }
        bind() from singleton { instance<WeatherDataBase>().alarmDao() }
        bind() from singleton { instance<WeatherDataBase>().favouriteDao() }
        //due to type inference context will be passed
        bind<ConnectivityInterceptor>() with singleton {
            ConnectivityInterceptorImpl(
                instance()
            )
        }
        bind() from singleton { OpenWeatherApiService(instance()) }
        bind<WeatherDataSource>() with singleton { WeatherDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<WeatherRepository>() with singleton {
            WeatherRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
       // bind<GpsLocationProvider>()with singleton { GpsLocationProviderImpl(instance(),instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        bind() from provider { FavouriteViewModelFactory(instance()) }
        bind() from provider { DetailsFragmentViewModelFactory(instance(), instance()) }
        bind() from provider { AlarmViewModelFactory(instance()) }
        bind() from provider { AlarmSettingsViewModelFactory(instance()) }
    }


    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        //set default values in the resouce by false if it isn't setted by the userr
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}
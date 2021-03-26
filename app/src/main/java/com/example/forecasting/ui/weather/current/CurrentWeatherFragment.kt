package com.example.forecasting.ui.weather.current

//import com.example.forecasting.data.provider.enums.TempUnitSystem

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecasting.R
import com.example.forecasting.data.provider.USE_DEVICE_LOCATION
import com.example.forecasting.data.provider.enums.TempUnitSystem
import com.example.forecasting.data.provider.enums.WindUnitSystem
import com.example.forecasting.ui.helpers.ScopedFragment
import com.example.forecasting.ui.weather.weak_forecast.day_list.ListAdapter
import com.example.forecasting.utilities.GlideApp
import com.example.forecasting.utilities.Helpers
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.io.IOException


private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
private val REQUEST_GPS_REQUEST_CODE = 35
private val DAY_DETAILS_REQUEST = 35

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private val fusedLocationClient: FusedLocationProviderClient by instance()

    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var prefEditor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }


    override fun onStart() {
        super.onStart()
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        if (preferences.getBoolean(USE_DEVICE_LOCATION, true)) {
            getLastLocation()
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        val navBar: CoordinatorLayout? =
            (activity as? AppCompatActivity)?.findViewById(R.id.coordinatorLayout_activity)
        navBar!!.visibility = View.VISIBLE
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)
        val adapter = HourListAdapter(viewModel)
        val dayAdapter = ListAdapter(viewModel)
        initializeUi(adapter, dayAdapter)

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {

            if (isProviderEnabled()) {
                fusedLocationClient?.lastLocation
                    ?.addOnCompleteListener(requireActivity()) { task ->
                        val location = task.result
                        if (location == null) {
                            requestNewLocation()
                        } else {
                            var latitude = location.latitude
                            var longitude = location.longitude
                            val sharedPref =
                                requireContext().applicationContext.getSharedPreferences(
                                    "location",
                                    Context.MODE_PRIVATE
                                )
                            val editor: SharedPreferences.Editor = sharedPref.edit()
                            editor.putString("MYLAT", latitude.toString())
                            editor.putString("MYLONG", longitude.toString())
                            editor.apply()
                            val geocoder = Geocoder(activity)
                            try {
                                val totalAdress = geocoder.getFromLocation(latitude, longitude, 1)
                                var address = totalAdress[0].getAddressLine(0)
                                Toast.makeText(activity, address, Toast.LENGTH_LONG)
                                    .show()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }


            } else {
                Log.i("gps", "willOpenGpsActivity")
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {

            Log.i("gps1", "no CheckPermission: ")
            requestPermission()
        }


    }

    private fun isProviderEnabled(): Boolean {
        Log.i("gps", "isProvidedEnabled")
        var locMgr = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }

    private fun checkPermissions(): Boolean {
        Log.i("gps1", "checkPermissions")
        var isPermissioned = false
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            isPermissioned = true
            Log.i("gps", "checkPermissions: " + "it's permissioned")
        }

        return isPermissioned
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GPS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("gps2", "فتحت الgps")
                getLastLocation()
            }

        }
    }

    private fun requestPermission() {
        Log.i("gps", "requestPermission ")
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            ,
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("gps", "requestPermissionResult ")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                //show dialog
                requestPermission();

            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocation() {
        Log.i("gps1", "requestNewLocation: ")
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10
        //   locationRequest.setFastestInterval(5000);
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.numUpdates = 1
        if (checkPermissions()) {
            fusedLocationClient!!.requestLocationUpdates(
                locationRequest,
                mLocationCallback,
                Looper.myLooper()
            )
        }
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.i("gps", "LocationCallBack")
            super.onLocationResult(locationResult)
            Log.i("location", "onLocationResult: ")

            val location = locationResult.lastLocation
            if (location != null && locationResult.locations.size > 0) {
                val latitude = location.latitude
                val longitude = location.longitude
                val sharedPref =
                    requireContext().getSharedPreferences("location", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putString("MYLAT", latitude.toString())
                editor.putString("MYLONG", longitude.toString())
                editor.apply()
            }

        }
    }

    private fun initializeUi(adapter: HourListAdapter, dayadapter: ListAdapter) {
        hour_Forecast_recyclerview.adapter = adapter
        hour_Forecast_recyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        hour_Forecast_recyclerview.setHasFixedSize(true)

        day_Forecast_recyclerview.adapter = dayadapter
        day_Forecast_recyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        day_Forecast_recyclerview.setHasFixedSize(true)


        viewModel.getNavigation.observe(this@CurrentWeatherFragment.viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { it1 ->
                val detailsAction =
                    CurrentWeatherFragmentDirections.detailsAction(DAY_DETAILS_REQUEST, it1, "")
                findNavController().navigate(detailsAction)
            }

        })
        launch {
            val currentWeather = viewModel.fetchCurrentWeather.await()
            currentWeather.observe(
                this@CurrentWeatherFragment.viewLifecycleOwner,
                Observer {
                    if (it == null) {
                        return@Observer
                    } else {
                        adapter.setList(it.hourly)
                        dayadapter.setList(it.daily)
                        updateLocation(it.zonedDateTime.zone.normalized().toString())
                        updateDateToToday(it.current.dt)
                        updateTemperatures(it.current.temp)
                        val weather = it.current.weather.get(0)
                        updateCondition(weather.description)
                        updatePressure(it.current.pressure)
                        updateHumidity(it.current.humidity)
                        updateVisibility(it.current.visibility)
                        updateWind(it.current.wind_speed)
                        updateCity(it.lat, it.lon,it.zonedDateTime.zone.normalized().toString())
                        if (Helpers.isNetworkAvailable(requireContext())) {
                            GlideApp.with(this@CurrentWeatherFragment)
                                .load("https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png")
                                .into(imageView_condition_icon)
                        }
                        group_success.visibility = View.VISIBLE
                        group_loading.visibility = View.INVISIBLE
                    }

                }
            )

        }
    }

    private fun updateCity(lat: Double, lon: Double,location: String) {
        val geocoder = Geocoder(activity)
        var cityName = location
        try {
            val totalAdress = geocoder.getFromLocation(lat, lon, 10)
            // var address = totalAdress[0].getAddressLine(0)
            if(totalAdress != null && totalAdress.size>0){
                for(adress in totalAdress){
                    if (adress.subAdminArea != null) {
                        cityName =adress.subAdminArea
                    }
                }

            }

        } catch (e: IOException) {
            cityName =location
        }
        tvCity.text = cityName
    }






    private fun updateWind(windSpeed: Double) {
        var abbreviation = "m/s"
        var wind = windSpeed

        if (viewModel.windUnitSystem == WindUnitSystem.KILOMETRE) {
            abbreviation = " Km/hr"
            wind = wind * 3.6
        } else if (viewModel.windUnitSystem == WindUnitSystem.MILE) {
            abbreviation = " mile/hr"
            wind = wind * 3.6
        }

        textView_wind.text = " " + wind.toInt() + abbreviation
    }


    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location


    }

    private fun updateDateToToday(dt: Long) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = requireContext().resources.getString(
            requireContext().resources.getIdentifier(
                "today_label", "string",
                requireContext().packageName
            )
        )
        val instant = Instant.ofEpochSecond(dt)
        val zoneId = ZoneId.of("UTC").normalized()
        date.text = ZonedDateTime.ofInstant(instant, zoneId).toLocalDate().toString()
    }

    private fun updateTemperatures(temperature: Double) {
        var abbreviation = " °K"
        var temp = temperature
        if (viewModel.tempUnitSystem == TempUnitSystem.CELSIUS) {
            abbreviation = " °c"
            temp = temp - 273.0
        } else if (viewModel.tempUnitSystem == TempUnitSystem.FAHRENHIT) {
            abbreviation = " °F"
            temp = ((temp - 273.15) * 9 / 5) + (32)
        }
        textView_temperature.text = " " + temp.toInt() + abbreviation
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }


    private fun updatePressure(pressure: Int) {
        textView_pressure.text = "$pressure hpa"
    }

    private fun updateHumidity(humidity: Int) {
        textView_humidity.text = " $humidity %"
    }

    private fun updateVisibility(visibilityDistance: Int) {
        textView_visibility.text = "$visibilityDistance m"
    }


}
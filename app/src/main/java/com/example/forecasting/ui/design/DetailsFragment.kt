package com.example.forecasting.ui.design

import android.location.Geocoder
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.forecasting.R
import com.example.forecasting.data.provider.enums.TempUnitSystem
import com.example.forecasting.data.provider.enums.WindUnitSystem
import com.example.forecasting.databinding.ActivityMainBinding
import com.example.forecasting.databinding.FragmentDetailsBinding
import com.example.forecasting.ui.MainActivity
import com.example.forecasting.utilities.GlideApp
import com.example.forecasting.utilities.Helpers
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.io.IOException


private val DAY_DETAILS_REQUEST = 35
private const val Favourite_DETAILS_REQUEST = 100

@Suppress("UNREACHABLE_CODE")
class DetailsFragment : Fragment(R.layout.fragment_details), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: DetailsFragmentViewModelFactory by instance()
    private lateinit var viewModel: DetailsFragmentViewModel
    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()

    private var cardPosition: Int = -1
    private lateinit var cityId: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        activateAnimation()

        val goAnnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.togo)
        binding.fabClose.setOnClickListener {
            binding.cardViewDetails.startAnimation(goAnnimation)
            binding.fabClose.startAnimation(goAnnimation)
            ViewCompat.animate(binding.cardViewDetails).setStartDelay(1000).alpha(0f).start()
            ViewCompat.animate(binding.fabClose).setStartDelay(1000).alpha(0f).start()
        }
    }

    private fun activateAnimation() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fromsmall)
        val lociNnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fromloci)
        binding.cardViewDetails.alpha = 0f
        binding.fabClose.alpha = 0f

        binding.cardViewDetails.alpha = 1f
        binding.cardViewDetails.startAnimation(animation)

        binding.fabClose.alpha = 1f
        binding.fabClose.startAnimation(lociNnimation)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailsFragmentViewModel::class.java)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val navBar: CoordinatorLayout? =
            (activity as? AppCompatActivity)?.findViewById(R.id.coordinatorLayout_activity)
        navBar!!.visibility = View.GONE



        if (args.type == DAY_DETAILS_REQUEST) {
            val currentObject = viewModel.getDetailsCurrentDay()
            cardPosition = args.position
            val currentDay = currentObject.daily.get(cardPosition)
            updateCity(
                currentObject.lat,
                currentObject.lon,
                currentObject.zonedDateTime.zone.normalized().toString()
            )
            updateTemperatures(currentDay.temp.day)
            updateDateToToday(currentObject.current.dt)
            updateCondition(currentDay.weather.get(0).description)
            updatePressure(currentDay.pressure)
            updateHumidity(currentDay.humidity)
            updateVisibility(currentDay.visibility)
            updateWind(currentDay.wind_speed)
            uploadImage(currentDay.weather.get(0).icon)
        } else {
            cityId = args.cityId
            val favLocation = viewModel.getFavouriteLocation(cityId)
            updateCity(
                favLocation.lat,
                favLocation.lon,
              favLocation.zonedDateTime.zone.normalized().toString()
            )
            updateTemperatures(favLocation.current.temp)
            updateDateToToday(favLocation.current.dt)
            updateCondition(favLocation.current.weather.get(0).description)
            updatePressure(favLocation.current.pressure)
            updateHumidity(favLocation.current.humidity)
            updateVisibility(favLocation.current.visibility)
            updateWind(favLocation.current.wind_speed)
            uploadImage(favLocation.current.weather.get(0).icon)
        }
    }

    private fun updateCity(lat: Double, lon: Double, location: String) {
        val geocoder = Geocoder(activity)
        var cityName = location
        try {
            val totalAdress = geocoder.getFromLocation(lat, lon, 10)
            if (totalAdress != null && totalAdress.size > 0) {
                for (adress in totalAdress) {
                    if (adress.subAdminArea != null) {
                        cityName = adress.subAdminArea
                    }
                }

            }

        } catch (e: IOException) {
            cityName = location
        }
        binding.tvCity1.text = cityName
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
        binding.textViewTemperature.text = " " + temp.toInt() + abbreviation
    }

    private fun updateDateToToday(dt: Long) {
        val instant = Instant.ofEpochSecond(dt)
        val zoneId = ZoneId.of("UTC").normalized()
        binding.date.text = ZonedDateTime.ofInstant(instant, zoneId).toLocalDate().toString()
    }

    private fun updateCondition(condition: String) {
        binding.textViewCondition1.text = condition
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

        binding.textViewWind.text = " " + wind.toInt() + abbreviation
    }

    private fun updatePressure(pressure: Int) {
        binding.textViewPressure.text = "$pressure hpa"
    }

    private fun updateHumidity(humidity: Int) {
        binding.textViewHumidity.text = " $humidity %"
    }

    private fun updateVisibility(visibilityDistance: Int) {
        binding.textViewVisibility.text = "$visibilityDistance m"
    }

    private fun uploadImage(id: String) {
        if (Helpers.isNetworkAvailable(requireContext())) {
            GlideApp.with(requireParentFragment())
                .load("https://openweathermap.org/img/wn/${id}@2x.png")
                .into(binding.imageViewConditionIcon)
        }
    }

}
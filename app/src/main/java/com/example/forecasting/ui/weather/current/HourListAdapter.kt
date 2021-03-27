package com.example.forecasting.ui.weather.current

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forecasting.R
import com.example.forecasting.data.local_db.entity.Hour
import com.example.forecasting.domain.use_case.enums.TempUnitSystem
import com.example.forecasting.utilities.GlideApp
import com.example.forecasting.utilities.Helpers
import kotlinx.android.synthetic.main.item_weather_hour_of_day.view.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class HourListAdapter(
    private val viewModel: CurrentWeatherViewModel
) : RecyclerView.Adapter<HourListAdapter.WeatherViewHolder>() {
    private var futureList = listOf<Hour>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_weather_hour_of_day, parent, false)
        return WeatherViewHolder(itemView)
    }

    override fun getItemCount() = futureList.size
    public fun setList(listItems: List<Hour>) {
        this.futureList = listItems
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindView(futureList[position], position)
    }


    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon_condition = itemView.image_View_Forecast_Icon_per_Hour
        val txt_date = itemView.text_View_Hour_Of_Day
        val txt_temperature = itemView.textViewTemp_per_hour

        fun bindView(currentItem: Hour, position: Int): Unit {
            updateTemperatures(currentItem.temp)
            val instant = Instant.ofEpochSecond(currentItem.dt)
            val zoneId = ZoneId.of("UTC").normalized()
            txt_date.text = ZonedDateTime.ofInstant(instant, zoneId).toLocalTime().toString()
            if (Helpers.isNetworkAvailable(itemView.context)) {
                GlideApp.with(txt_date.context)
                    .load("https://openweathermap.org/img/wn/${currentItem.weather.get(0).icon}@2x.png")
                    .into(icon_condition)
            }
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
            txt_temperature.text = " " + temp.toInt() + abbreviation
        }


    }
}
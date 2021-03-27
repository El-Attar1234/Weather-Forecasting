package com.example.forecasting.ui.weather.current

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forecasting.R
import com.example.forecasting.data.local_db.entity.Day
import com.example.forecasting.domain.use_case.enums.TempUnitSystem
import com.example.forecasting.utilities.GlideApp
import com.example.forecasting.utilities.Helpers
import kotlinx.android.synthetic.main.day_weather.view.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class ListAdapter(
private val viewModel: CurrentWeatherViewModel
):RecyclerView.Adapter<ListAdapter.WeatherViewHolder>()  {
    private var futureList= listOf<Day>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        context=parent.context
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.day_weather,parent,false)
        return WeatherViewHolder(itemView)
    }

    override fun getItemCount()=futureList.size
    public fun setList(listItems:List<Day>){
        this.futureList=listItems
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindView(futureList[position],position)
    }



    inner class WeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val icon_condition= itemView.weather_image_view_day
        val txt_date= itemView.day_name_text_view
        val txt_temperature= itemView.temp_text_view_day
        val rootView=itemView.rootView
        val txt_condition= itemView.day_description_text_view

        fun bindView(currentItem:Day,position:Int):Unit{
          //  imageView.setImageResource(currentItem.imageResource)
            updateTemperatures(currentItem.temp.day)
            txt_condition.text=currentItem.weather.get(0).description.toString()
            val instant = Instant.ofEpochSecond(currentItem.dt)
            val zoneId = ZoneId.of("UTC").normalized()
            txt_date.text=ZonedDateTime.ofInstant(instant, zoneId).toLocalDate().toString()
            if(Helpers.isNetworkAvailable(itemView.context)){
            GlideApp.with(txt_temperature.context)
                .load("https://openweathermap.org/img/wn/${currentItem.weather.get(0).icon}@2x.png")
                .into(icon_condition)}
            itemView.setOnClickListener{
                viewModel.willNavigate(position)
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

    interface OnItemClickListener {
        fun onItemClick(dt: Long)
    }


}
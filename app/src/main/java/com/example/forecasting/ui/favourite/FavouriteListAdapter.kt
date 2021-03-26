package com.example.forecasting.ui.favourite

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forecasting.R
import com.example.forecasting.data.local_db.entity.FavouriteWeatherResponse
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.favourite_item.view.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.io.IOException

class FavouriteListAdapter(
    private val viewModel: FavouriteViewModel
) : RecyclerView.Adapter<FavouriteListAdapter.WeatherViewHolder>() {
    private var favouriteList = listOf<FavouriteWeatherResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.favourite_item, parent, false)
        return WeatherViewHolder(itemView)
    }

    override fun getItemCount() = favouriteList.size
    public fun setList(listItems: List<FavouriteWeatherResponse>) {
        this.favouriteList = listItems
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindView(favouriteList[position], position)
    }


    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationCity = itemView.location_city
        val locationRest = itemView.location_rest
        val icon_delete = itemView.loc_delete
        // val txt_condition= itemView.textView_condition

        fun bindView(currentItem: FavouriteWeatherResponse, position: Int): Unit {
            //  imageView.setImageResource(currentItem.imageResource)
          val city= getCity(currentItem.lat,currentItem.lon,currentItem.timezone,itemView.context)
            locationCity.text =city
            locationRest.text = currentItem.current.weather.get(0).description
            icon_delete.setOnClickListener {
                viewModel.pressOnDeleteIcon(currentItem.cityID.toString())
            }
            itemView.setOnClickListener {
                viewModel.showDetails(currentItem.cityID.toString())
            }
        }


    }
}

private fun getCity(lat: Double, lon: Double,zone:String,context: Context):String {
    val geocoder = Geocoder(context)
    var cityName = ""
    try {
        val totalAdress = geocoder.getFromLocation(lat, lon, 10)
        if(totalAdress != null && totalAdress.size>0){
            for(adress in totalAdress){
                if (adress.subAdminArea != null) {
                    cityName =adress.subAdminArea
                }
            }

        }
    } catch (e: IOException) {
        cityName =zone
    }
   return cityName
}

package com.example.forecasting.data.local_db.converters

import androidx.room.TypeConverter
import com.example.forecasting.data.local_db.entity.CurrentweatherResponse
import com.example.forecasting.data.local_db.entity.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class WeatherStringConverter {
    @TypeConverter
    fun fromArrayListToString(notes: ArrayList<Weather>): String {
        return Gson().toJson(notes)
    }

    @TypeConverter
    fun fromStringToArrayList(strNote: String?): ArrayList<Weather> {
        val listType = object :
            TypeToken<ArrayList<Weather>>() {}.type
        return Gson().fromJson(strNote, listType)
    }
}

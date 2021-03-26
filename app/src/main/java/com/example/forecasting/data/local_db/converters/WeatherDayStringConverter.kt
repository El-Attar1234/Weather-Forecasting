package com.example.forecasting.data.local_db.converters

import androidx.room.TypeConverter
import com.example.forecasting.data.local_db.entity.Weather
import com.example.forecasting.data.local_db.entity.WeatherDay
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class WeatherDayStringConverter {
    @TypeConverter
    fun fromArrayListToString(notes: ArrayList<WeatherDay>): String {
        return Gson().toJson(notes)
    }

    @TypeConverter
    fun fromStringToArrayList(strNote: String?): ArrayList<WeatherDay> {
        val listType = object :
            TypeToken<ArrayList<WeatherDay>>() {}.type
        return Gson().fromJson(strNote, listType)
    }
}
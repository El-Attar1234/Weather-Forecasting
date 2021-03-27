package com.example.forecasting.data.local_db.converters

import androidx.room.TypeConverter
import com.example.forecasting.data.local_db.entity.Day

import com.example.forecasting.data.local_db.entity.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class DayStringConverter {
    @TypeConverter
    fun fromArrayListToString(list: ArrayList<Day>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToArrayList(strList: String?): ArrayList<Day> {
        val listType = object :
            TypeToken<ArrayList<Day>>() {}.type
        return Gson().fromJson(strList, listType)
    }
}
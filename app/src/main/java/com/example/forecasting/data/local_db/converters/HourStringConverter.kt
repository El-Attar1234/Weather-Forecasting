package com.example.forecasting.data.local_db.converters

import androidx.room.TypeConverter
import com.example.forecasting.data.local_db.entity.Day
import com.example.forecasting.data.local_db.entity.Hour
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class HourStringConverter {
    @TypeConverter
    fun fromArrayListToString(list: ArrayList<Hour>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToArrayList(strList: String?): ArrayList<Hour> {
        val listType = object :
            TypeToken<ArrayList<Hour>>() {}.type
        return Gson().fromJson(strList, listType)
    }
}
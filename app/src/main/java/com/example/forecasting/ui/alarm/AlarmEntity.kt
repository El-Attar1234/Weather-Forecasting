package com.example.forecasting.ui.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
data class AlarmEntity(

    val type:String,
    val description:String,
    val time:String,
    val date:String,
    @PrimaryKey()
val alarm_id:Int=0
){

}
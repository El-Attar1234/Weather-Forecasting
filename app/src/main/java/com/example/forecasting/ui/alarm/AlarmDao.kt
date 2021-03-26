package com.example.forecasting.ui.alarm

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecasting.data.local_db.entity.Day


@Dao
interface AlarmDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarmEntity: AlarmEntity):Long

    @Query("delete from alarm_table where alarm_id=:id")
     fun deleteAlarm(id:Int)

    @Query("select * from alarm_table ")
    suspend fun getAllAlarms():List<AlarmEntity>
 @Query("select * from alarm_table where alarm_id=:id")
  fun getAlarmById(id:Int):AlarmEntity




}
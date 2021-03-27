package com.example.forecasting.ui.alarm.alarm_seetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecasting.domain.repo.WeatherRepository
import com.example.forecasting.data.local_db.entity.AlarmEntity
import kotlinx.coroutines.launch

class AlarmSettingsViewModel(private val weatherRepository: WeatherRepository
): ViewModel() {


    fun insertAlarm(alarmEntity: AlarmEntity){
        viewModelScope.launch {
            weatherRepository.insertAlarm(alarmEntity)
        }
    }

    fun getAlarmById(id:Int): AlarmEntity {
        return weatherRepository.getAlarmById(id)
    }
    fun deleteAlarm(id:Int){
        weatherRepository.deleteAlarm(id)
    }





}
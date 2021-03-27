package com.example.forecasting.ui.alarm.alarm_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecasting.domain.repo.WeatherRepository
import com.example.forecasting.utilities.Event
import com.example.forecasting.data.local_db.entity.AlarmEntity
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private var mutableAllAlarms = MutableLiveData<List<AlarmEntity>>()
    private var mutablealarmDelete = MutableLiveData<Event<Int>>()
    private var mutablealarmEdit = MutableLiveData<Event<Int>>()

    init {
        viewModelScope.launch {
            val allAlarms = weatherRepository.getAllAlarms()
            mutableAllAlarms.value = allAlarms
        }
    }


    fun pressOnDeleteIcon(id: Int) {
        mutablealarmDelete.value = Event(id)
    }

    fun pressOnEditIcon(id: Int) {
        mutablealarmEdit.value = Event(id)
    }


    fun deleteAlarm(id: Int) {
        weatherRepository.deleteAlarm(id)
    }

    fun getAllArams() {
        viewModelScope.launch {
            val allAlarms = weatherRepository.getAllAlarms()
            mutableAllAlarms.value = allAlarms
        }

    }


    val alamListLiveData: LiveData<List<AlarmEntity>>
        get() = mutableAllAlarms

    val alamDeletedId: LiveData<Event<Int>>
        get() = mutablealarmDelete

    val alamEditedId: LiveData<Event<Int>>
        get() = mutablealarmEdit


//    val alamListLiveData:LiveData<List<AlarmEntity>> get() = alamListLiveData
}
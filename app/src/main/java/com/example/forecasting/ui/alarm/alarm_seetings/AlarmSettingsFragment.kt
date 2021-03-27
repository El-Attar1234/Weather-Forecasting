package com.example.forecasting.ui.alarm.alarm_seetings

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.example.forecasting.R
import com.example.forecasting.data.local_db.entity.AlarmEntity
import com.example.forecasting.databinding.FragmentAlarmSettingsBinding
import kotlinx.android.synthetic.main.fragment_alarm_settings.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*
import java.util.concurrent.TimeUnit


class AlarmSettingsFragment : Fragment(R.layout.fragment_alarm_settings), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: AlarmSettingsViewModelFactory by instance()
    private lateinit var viewModel: AlarmSettingsViewModel
    private val args: AlarmSettingsFragmentArgs by navArgs()
    private lateinit var binding: FragmentAlarmSettingsBinding


    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    var hour = 0
    var minuite: Int = 0
    var mySelectedIndex = -1
    var deletedId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAlarmSettingsBinding.bind(view)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AlarmSettingsViewModel::class.java)
        if (args.alarmId != -1) {
            editAlarm(args.alarmId)
            deletedId = args.alarmId
        }
        repeat_switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (binding.radioGroup.isVisible) {
                binding.radioGroup.setVisibility(View.GONE)
            } else {
                binding.radioGroup.setVisibility(View.VISIBLE)
            }
            binding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radioButton =
                    group.findViewById<View>(checkedId) as RadioButton
                mySelectedIndex = binding.radioGroup.indexOfChild(radioButton)
            })
        })
        tv_date_label.setOnClickListener {
            selectDate()
        }
        tv_time_label.setOnClickListener {
            selectTime()
        }
        btn_addAlarm.setOnClickListener {
            saveAlarm(deletedId)
        }
    }

    private fun editAlarm(alarmId: Int) {
        val editedAlarm = viewModel.getAlarmById(alarmId)
        alarm_description.setText(editedAlarm.description)
        binding.txtDate.setText(editedAlarm.date)
        binding.txtTime.setText(editedAlarm.time)
        binding.btnAddAlarm.setText(R.string.updateAlarm)

    }

    private fun selectTime() {
        val c = Calendar.getInstance()
        hour = c[Calendar.HOUR_OF_DAY]
        minuite = c[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            context, OnTimeSetListener { view, hourOfDay, minute ->
                val calendar = Calendar.getInstance()
                calendar.set(0, 0, 0, hourOfDay, minute)
                txt_time.text = DateFormat.format("hh:mm:aa", calendar)
                hour = hourOfDay - hour
                minuite = minute - minuite
            }, 12, 0, false
        )
        timePickerDialog.updateTime(hour, minuite)
        timePickerDialog.show()
    }

    private fun selectDate() {
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                txt_date.text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                mYear = year - mYear
                mMonth = monthOfYear - mMonth
                mDay = dayOfMonth - mDay
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }


    private fun saveAlarm(deletedId: Int) {
        val diffTime =
            (hour * 60 + minuite + mDay * 24 * 60 + mMonth * 30 * 24 * 60 + mYear * 12 * 30 * 24 * 60) * 60 * 1000

        if (diffTime <= 0) {
            Toast.makeText(
                requireContext(),
                "you should enter valid and complete data",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            val alarmEntity =
                AlarmEntity(
                    returnMain(mySelectedIndex),
                    alarm_description.text.toString(),
                    txt_time.text.toString(),
                    txt_date.text.toString(),
                    diffTime
                )
            viewModel.deleteAlarm(deletedId)
            viewModel.insertAlarm(alarmEntity)

            /* val alarmConstraints = Constraints.Builder()
                 .setRequiresBatteryNotLow(true)
                 .setRequiredNetworkType(NetworkType.CONNECTED)
                 .setRequiresCharging(true)
                 .setRequiresStorageNotLow(true)
                 .build()*/

            val inputData = Data.Builder()
                .putString("type", returnMain(mySelectedIndex))
                .putString("description", alarm_description.text.toString())
                .putInt("key", diffTime)
                .putInt("index", mySelectedIndex)
                .build()

            val uploadWorkRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
                .setInputData(inputData)
                //.setConstraints(alarmConstraints)
                .setInitialDelay(diffTime.toLong(), TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest)
            navigateToAlarmList()

        }
    }

    private fun navigateToAlarmList() {
        val alarmListAction = AlarmSettingsFragmentDirections.alarmListActions()
        findNavController().navigate(alarmListAction)
    }

    fun returnMain(index: Int): String {
        var type = retunStringValue("alarm_rain")
        when (index) {
            0 -> type = retunStringValue("alarm_rain")
            1 -> type = retunStringValue("alarm_temp")
            2 -> type = retunStringValue("alarm_wind")
            3 -> type = retunStringValue("alarm_fog")
            5 -> type = retunStringValue("alarm_cloud")
            6 -> type = retunStringValue("alarm_thunderstorm")
        }
        return type
    }

    fun retunStringValue(name: String): String {
        val value = requireContext().resources.getString(
            requireContext().resources.getIdentifier(
                name, "string",
                requireContext().packageName
            )
        )
        return value
    }
}





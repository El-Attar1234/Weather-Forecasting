package com.example.forecasting.ui.alarm.alarm_list

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecasting.R
import com.example.forecasting.databinding.AlarmFragmentBinding
import com.example.forecasting.ui.favourite.FavouriteListAdapter
import com.example.forecasting.ui.weather.current.CurrentWeatherViewModel
import com.example.forecasting.ui.weather.current.CurrentWeatherViewModelFactory
import com.example.forecasting.utilities.Event
import kotlinx.android.synthetic.main.alarm_fragment.*
import kotlinx.android.synthetic.main.favourite_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AlarmFragment : Fragment(R.layout.alarm_fragment), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: AlarmViewModelFactory by instance()
    private lateinit var viewModel: AlarmViewModel
 private lateinit var binding:AlarmFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= AlarmFragmentBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(AlarmViewModel::class.java)
        val adapter = AlarmListAdapter(viewModel)
        initializeUi(adapter)
        (activity as? AppCompatActivity)?.supportActionBar?.title ="Alarm List"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =null
    }

    private fun initializeUi(adapter: AlarmListAdapter) {
       binding.alarmRecyclerView.adapter = adapter
        binding.alarmRecyclerView.layoutManager =
            LinearLayoutManager(activity)
        binding.alarmRecyclerView.setHasFixedSize(true)


        viewModel.alamListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)

        })
        viewModel.alamDeletedId.observe(viewLifecycleOwner, Observer {
            openDeletedDialog(it)
        })
        viewModel.alamEditedId.observe(viewLifecycleOwner, Observer {
            navigateToEditAlarm(it)
        })
    }

    private fun navigateToEditAlarm(it: Event<Int>?) {
        it?.getContentIfNotHandled()?.let { it1 -> openAlarmSettings(it1) }
    }

    private fun openDeletedDialog(it: Event<Int>?) {

        val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
        builder1.setMessage(getResourceValue("delete_label"))
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            getResourceValue("ok_label"),
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                it?.getContentIfNotHandled()?.let { it1 -> viewModel.deleteAlarm(it1) }
                viewModel.getAllArams()
            })

        builder1.setNegativeButton(
            getResourceValue("no_label"),
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllArams()
    }

    private fun openAlarmSettings(alarmId: Int) {
        val detailsAction =
            AlarmFragmentDirections.alarmSettingsAction(alarmId)
        findNavController().navigate(detailsAction)
    }

    fun getResourceValue(label:String):String{
        return requireContext().resources.getString(
            requireContext().resources.getIdentifier(
                label, "string",
                requireContext().packageName
            )
        )
    }

}
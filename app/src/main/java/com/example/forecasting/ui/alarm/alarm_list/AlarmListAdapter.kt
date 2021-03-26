package com.example.forecasting.ui.alarm.alarm_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forecasting.R
import com.example.forecasting.data.local_db.entity.Hour
import com.example.forecasting.ui.alarm.AlarmEntity
import com.example.forecasting.ui.weather.current.HourListAdapter
import com.example.forecasting.utilities.GlideApp
import kotlinx.android.synthetic.main.alarm_item.view.*
import kotlinx.android.synthetic.main.item_weather_hour_of_day.view.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class AlarmListAdapter (
    private val viewModel:AlarmViewModel
) : RecyclerView.Adapter<AlarmListAdapter.WeatherViewHolder>()  {
    private var alarmList= listOf<AlarmEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.alarm_item,parent,false)
        return WeatherViewHolder(itemView)
    }

    override fun getItemCount()=alarmList.size
    public fun setList(listItems:List<AlarmEntity>){
        this.alarmList=listItems
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindView(alarmList[position],position)
    }



    inner class WeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txt_state= itemView.alarm_state
        val txt_desc= itemView.alarm_description
        val txt_time= itemView.alarm_time
        val txt_date= itemView.alarm_date
         val delete_btton=itemView.alarm_delete
        val edit_btton=itemView.alarm_edit


        fun bindView(currentItem: AlarmEntity, position:Int):Unit{
             txt_state.text= currentItem.type.toString()
             txt_desc.text= currentItem.description.toString()
             txt_time.text= currentItem.time.toString()
             txt_date.text= currentItem.date.toString()
            delete_btton.setOnClickListener{
        viewModel.pressOnDeleteIcon(currentItem.alarm_id)
            }
            edit_btton.setOnClickListener{
         viewModel.pressOnEditIcon(currentItem.alarm_id)
            }
        }


    }
}
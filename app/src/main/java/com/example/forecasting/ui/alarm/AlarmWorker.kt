package com.example.forecasting.ui.alarm

import android.app.*
import android.app.Notification.DEFAULT_VIBRATE
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.forecasting.R
import com.example.forecasting.data.local_db.WeatherDataBase
import com.example.forecasting.data.repository.WeatherRepository
import com.example.forecasting.ui.alarm.alarm_seetings.AlarmSettingsViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class AlarmWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams),
    KodeinAware {
    override val kodein: Kodein by closestKodein { context }
    private val weatherRepository: WeatherRepository by instance()

    private val context = context.applicationContext as Application
    override fun doWork(): Result {
        val type = inputData.getString("type")
        val desc = inputData.getString("description")
        val key = inputData.getInt("key", 100)
        val index = inputData.getInt("index", -1)
        val db = WeatherDataBase(context)

        val main_description = db.currentWeatherDao().getCurrentWeatherResponseWithoutLiveData().current.weather.get(0).main

        if (main_description.equals(weatherMain(index))) {
            createNotificationChannels()
            sendOnChannel2(type.toString())
            var mMediaPlayer = MediaPlayer()
            mMediaPlayer = MediaPlayer.create(context,R.raw.alarm_sound)
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer.start()
            Thread.sleep(10000)
            mMediaPlayer.stop()
        }
     //   val main_description = currentObject.current.weather.get(0).main
        Log.i("alarm", "4")
      //  Log.i("alarm", "${main_description}")
        val currentObject1 = weatherRepository.getCurrentWeatherResponseWithoutLiveData()
        Log.i("alarm", "5")
        val main_description1 = currentObject1.current.weather.get(0).main
        Log.i("alarm", "6")



        weatherRepository.deleteAlarm(key)
        return Result.success()

    }

fun weatherMain(index:Int):String{
    var type = ""
    when (index) {
        0 -> type = "Rain"
        1 -> type = "Temperature"
        2 -> type ="Wind"
        3 -> type ="ِSnow"
        5 -> type ="Clouds"
        6 -> type ="Thunderstorm"
    }
    return type
}
    private fun createNotificationChannels() {
        // create android channel
        var androidChannel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            androidChannel =
                NotificationChannel("2", "channel", NotificationManager.IMPORTANCE_HIGH)
            androidChannel.enableLights(true)
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true)
            // Sets the notification light color for notifications posted to this channel
            androidChannel.lightColor = Color.GREEN
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager: NotificationManager =
                context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(androidChannel)
        }
    }

    private fun sendOnChannel2(message: String) {

        val notificationManager = NotificationManagerCompat.from(applicationContext);
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification: Notification =
            NotificationCompat.Builder(applicationContext, "2").setDefaults(DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_mostly_cloudy)
                .setContentTitle(message)
                .setContentText("watch out now it's $message in your area")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setSound(alarmSound)
              .setDefaults(Notification.DEFAULT_SOUND)
                .build()
        notificationManager.notify(2, notification)
    }

}






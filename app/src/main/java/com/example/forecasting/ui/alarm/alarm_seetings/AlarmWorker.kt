package com.example.forecasting.ui.alarm.alarm_seetings

import android.app.*
import android.app.Notification.DEFAULT_VIBRATE
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.forecasting.R
import com.example.forecasting.data.local_db.db.WeatherDataBase
import com.example.forecasting.domain.repo.WeatherRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
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

        val main_description =
            weatherRepository.getCurrentWeatherResponseWithoutLiveData().current.weather.get(0).main

        if (main_description.equals(weatherMain(index))) {
            createNotificationChannels()
            sendOnChannel2(type.toString(), desc.toString())
            var mMediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound)
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer.start()
            Thread.sleep(6000)
            mMediaPlayer.stop()
        }
        weatherRepository.deleteAlarm(key)
        return Result.success()

    }

    fun weatherMain(index: Int): String {
        var type = ""
        when (index) {
            0 -> type = "Rain"
            1 -> type = "Temperature"
            2 -> type = "Wind"
            3 -> type = "ِSnow"
            5 -> type = "Clouds"
            6 -> type = "Thunderstorm"
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

    private fun sendOnChannel2(message: String, content: String) {

        val notificationManager = NotificationManagerCompat.from(applicationContext);
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification: Notification =
            NotificationCompat.Builder(applicationContext, "2").setDefaults(DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_mostly_cloudy)
                .setContentTitle(message)
                .setContentText(content)
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






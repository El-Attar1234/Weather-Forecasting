package com.example.forecasting.ui.weather_radar

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.forecasting.R
import kotlinx.android.synthetic.main.fragment_weather_radar.*
import kotlinx.android.synthetic.main.fragment_weather_radar.view.*

class RadarActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radar)
        val sharedPref =getSharedPreferences("location", Context.MODE_PRIVATE)
    val lat=sharedPref.getString("MYLAT", "30.7114093")!!
        val lon=sharedPref.getString("MYLONG", "31.6106543")!!
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(
            "file:///android_asset/map.html?lat=" + lat + "&lon="
                    + lon + "&appid=" + "3e29e62e2ddf6dd3d2ebd28aed069215" + "&zoom=" + 7
        )
        navigationBar.setOnNavigationItemSelectedListener{

                item ->
            when (item.itemId) {
                R.id.map_clouds -> {
                    webView.loadUrl(
                        "javascript:map.removeLayer(rainLayer);map.removeLayer(windLayer);map.removeLayer(tempLayer);"
                                + "map.addLayer(cloudsLayer);"
                    )
                }
                R.id.map_rain -> {
                    webView.loadUrl(
                        "javascript:map.removeLayer(cloudsLayer);map.removeLayer(windLayer);map.removeLayer(tempLayer);"
                                + "map.addLayer(rainLayer);"
                    )
                }
                R.id.map_wind -> {
                    webView.loadUrl(
                        "javascript:map.removeLayer(cloudsLayer);map.removeLayer(rainLayer);map.removeLayer(tempLayer);"
                                + "map.addLayer(windLayer);"
                    )
                }
                R.id.map_temperature -> {
                    webView.loadUrl(
                        "javascript:map.removeLayer(cloudsLayer);map.removeLayer(windLayer);map.removeLayer(rainLayer);"
                                + "map.addLayer(tempLayer);"
                    )
                }
            }
            true
            // setMapState(it.itemId)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                setMapState(0)
            }
    }
}
    private fun setMapState(item: Int) {
        when (item) {
            R.id.map_clouds -> webView.loadUrl(
                "javascript:map.removeLayer(rainLayer);map.removeLayer(windLayer);map.removeLayer(tempLayer);"
                        + "map.addLayer(cloudsLayer);"
            )
            R.id.map_rain -> webView.loadUrl(
                "javascript:map.removeLayer(cloudsLayer);map.removeLayer(windLayer);map.removeLayer(tempLayer);"
                        + "map.addLayer(rainLayer);"
            )
            R.id.map_wind -> webView.loadUrl(
                "javascript:map.removeLayer(cloudsLayer);map.removeLayer(rainLayer);map.removeLayer(tempLayer);"
                        + "map.addLayer(windLayer);"
            )
            R.id.map_temperature ->{
                Log.i("radar", "temp: ")
                Toast.makeText(this,"Temp",Toast.LENGTH_LONG).show()
                webView.loadUrl(
                "javascript:map.removeLayer(cloudsLayer);map.removeLayer(windLayer);map.removeLayer(rainLayer);"
                        + "map.addLayer(tempLayer);"
            )}
            else -> Log.w("WeatherMap", "Layer not configured")
        }
    }
}
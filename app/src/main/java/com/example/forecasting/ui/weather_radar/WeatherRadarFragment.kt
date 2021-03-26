package com.example.forecasting.ui.weather_radar

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.forecasting.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_weather_radar.*
import kotlinx.android.synthetic.main.fragment_weather_radar.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherRadarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherRadarFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
   val v=inflater.inflate(R.layout.fragment_weather_radar, container, false)

        val muWebView=v.webView
        muWebView.settings.javaScriptEnabled = true
        muWebView.loadUrl(
            "file:///android_asset/map.html?lat=" + "30" + "&lon="
                    + "31" + "&appid=" + "3e29e62e2ddf6dd3d2ebd28aed069215" + "&zoom=" + 7
        )
        return v

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeUi() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                    //setMapState(0)
            }
        }


        navigationBar.setOnNavigationItemSelectedListener{

                item ->
            when (item.itemId) {
                R.id.map_clouds -> {
                    setMapState(0)
                }
                R.id.map_rain -> {
                    setMapState(1)
                }
                R.id.map_wind -> {
                    setMapState(2)
                }
                R.id.map_temperature -> {
                    setMapState(3)
                }
            }
            true
      // setMapState(it.itemId)
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
            R.id.map_temperature -> webView.loadUrl(
                "javascript:map.removeLayer(cloudsLayer);map.removeLayer(windLayer);map.removeLayer(rainLayer);"
                        + "map.addLayer(tempLayer);"
            )
            else -> Log.w("WeatherMap", "Layer not configured")
        }
    }


}
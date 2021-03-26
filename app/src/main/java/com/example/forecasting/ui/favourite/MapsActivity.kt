package com.example.forecasting.ui.favourite

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.forecasting.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private  var  mark: LatLng? =null
    private lateinit var state:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        state = intent.getStringExtra("case").toString()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap .mapType = GoogleMap.MAP_TYPE_NORMAL;
        mMap .setOnMapClickListener {
            mMap .clear()
            mark = LatLng(it.latitude, it.longitude)
            mMap .addMarker(MarkerOptions().position(mark!!).title("mark"))
            mMap .moveCamera(CameraUpdateFactory.newLatLng(mark))
            Log.i("map", "onMapReady"+mark?.latitude.toString())
            Log.i("map", "onMapReady"+mark?.longitude.toString())
        }
    }



    @SuppressLint("CommitPrefEdits")
    override fun onDestroy() {
        super.onDestroy()
if(mark?.latitude!=null &&mark?.longitude!=null){
    if(state == "custom"){
        val sharedPref =applicationContext.getSharedPreferences("location", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("MYLAT",mark?.latitude.toString())
        editor.putString("MYLONG",mark?.longitude.toString())
        editor.apply()
        val lat=  this.applicationContext.getSharedPreferences("location", Context.MODE_PRIVATE).getString("MYLAT", "30")
        Log.i("fetch", "onDestroy: "+lat)
    }else if(state == "favourite"){
        Log.i("map", "onDestroy: ${mark?.latitude.toString()} ${mark?.longitude.toString()}")
        val sharedPref =applicationContext.getSharedPreferences("favourite", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("MYLAT_FAV",mark?.latitude.toString())
        editor.putString("MYLONG_FAV",mark?.longitude.toString())
        editor.apply()
    }
}


    }
}
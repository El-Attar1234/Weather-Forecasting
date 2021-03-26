package com.example.forecasting.data.provider.enums

enum class WindUnitSystem {
METRE,KILOMETRE,MILE
}

/*
package com.example.forecast_mvvm

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.forecast_mvvm.screens.favourite.FavouriteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MyMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private  var  mark: LatLng? =null
    private lateinit var state:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_map)

        state = intent.getStringExtra("state").toString()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL;
        map.setOnMapClickListener {
            map.clear()
            mark = LatLng(it.latitude, it.longitude)
            map.addMarker(MarkerOptions().position(mark!!).title("mark"))
            map.moveCamera(CameraUpdateFactory.newLatLng(mark))
        }

    }

    @SuppressLint("CommitPrefEdits")
    override fun onDestroy() {
        super.onDestroy()

        if(state == "fav" && mark != null){
            val favouriteViewModel = FavouriteViewModel(application)
            favouriteViewModel.saveFavouriteCoord(mark!!.latitude, mark!!.longitude)

        }else if(state == "set"){

            val sharedPref = getSharedPreferences("location", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString("lat",mark?.latitude.toString())
            editor.putString("lon",mark?.longitude.toString())

        }else{
            Log.i("TAG", "onDestroy: no mark")
        }
    }

}
*/
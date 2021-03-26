package com.example.forecasting.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Helpers {

    companion object{
        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }


    }
}
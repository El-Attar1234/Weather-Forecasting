package com.example.forecasting.data.network.connection_interceptor

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.forecasting.R
import com.example.forecasting.data.network.connection_interceptor.ConnectivityInterceptor
import com.example.forecasting.utilities.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response


class ConnectivityInterceptorImpl(context: Context) :
    ConnectivityInterceptor {
    val appContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isConnected()){
            Log.i("error", "no net ya mahmoud")
            val builder = AlertDialog.Builder(appContext) //create dialog builder
           builder.setMessage("Dialog message") //set dialog message
                .setTitle("Dialog title"); //set dialog title
            builder.setNeutralButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss() //add Ok button, when clicked close the dialog
            })
            builder.create().show()//show dialog
            //here i later will show dialog
            //throw NoConnectionException()
        }
        return chain.proceed(chain.request())
    }

    private fun isConnected(): Boolean {
        var status = false
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (cm!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ))
            } else {
                val activeNetwork = cm.activeNetworkInfo
                return activeNetwork != null && (activeNetwork.type === ConnectivityManager.TYPE_WIFI||activeNetwork.type === ConnectivityManager.TYPE_MOBILE)
            }
        }
        return false
    }
}
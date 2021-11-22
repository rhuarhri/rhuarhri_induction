package com.three_squared.rhuarhri_induction.online

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

/*
This class is used to check if the smartphone has an internet connection
 */
class ConnectionChecker @Inject constructor(private val context : Context) {

    fun check(): Boolean {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability = cm.getNetworkCapabilities(cm.activeNetwork)

        return if (capability != null) {
            capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    //capability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                    capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            false
        }
    }
}
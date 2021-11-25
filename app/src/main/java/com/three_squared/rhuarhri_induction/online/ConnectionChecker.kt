package com.three_squared.rhuarhri_induction.online

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

/*
This class is used to check if the smartphone has an internet connection
 */
class ConnectionChecker @Inject constructor(private val context : Context) {

    fun check(): ConnectionType {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability = cm.getNetworkCapabilities(cm.activeNetwork)

        return if (capability != null) {
            when {
                capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    ConnectionType.WEAK
                }
                capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    ConnectionType.STRONG
                }
                else -> {
                    ConnectionType.NONE
                }
            }
        } else {
            ConnectionType.NONE
        }
    }
}

enum class ConnectionType {
    STRONG,
    WEAK,
    NONE
}
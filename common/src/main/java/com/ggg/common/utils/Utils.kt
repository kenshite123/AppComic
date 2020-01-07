package com.ggg.common.utils

import android.app.Activity
import android.content.Context
import java.text.DecimalFormat
import android.net.ConnectivityManager

class Utils {
    companion object {
        fun formatNumber(number: Long) : String {
            val dec = DecimalFormat("#,###")
            return dec.format(number)
        }

        fun isAvailableNetwork(activity: Activity): Boolean{
            val connectivityManager= activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo=connectivityManager.activeNetworkInfo
            return  networkInfo!=null && networkInfo.isConnected
        }
    }
}
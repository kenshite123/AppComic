package com.ggg.common.utils

import android.content.Context
import android.net.ConnectivityManager
import com.ggg.common.GGGAppInterface

class CommonUtils {
    companion object {
        fun isInternetAvailable(): Boolean {
            val cm = GGGAppInterface.gggApp.ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
    }
}
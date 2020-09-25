package com.ggg.common.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat


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

        fun md5(s: String): String {
            val MD5 = "MD5"
            try { // Create MD5 Hash
                val digest = MessageDigest
                        .getInstance(MD5)
                digest.update(s.toByteArray())
                val messageDigest = digest.digest()
                // Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2) h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}
package com.ggg.home.utils

import java.text.DecimalFormat

class Utils {
    companion object {
        fun formatNumber(number: Long) : String {
            val dec = DecimalFormat("#,###")
            return dec.format(number)
        }
    }
}
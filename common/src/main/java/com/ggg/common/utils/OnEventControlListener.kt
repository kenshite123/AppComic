package com.ggg.common.utils

import android.view.View

interface OnEventControlListener {
    fun onEvent(eventAction: Int, control: View?, data: Any?)
}
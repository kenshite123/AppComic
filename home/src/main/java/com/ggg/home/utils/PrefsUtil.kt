package com.ggg.home.utils

import android.content.Context
import android.content.SharedPreferences
import com.ggg.common.GGGAppInterface
import java.util.*


class PrefsUtil private constructor() {


    private val locale: Locale? = null


    private val prefs: SharedPreferences
    private val editor: SharedPreferences.Editor


    init {
        println("This ($this) is a singleton")
        prefs = GGGAppInterface.gggApp.ctx.getSharedPreferences("home.preference.data", Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    private object Holder {
        val INSTANCE = PrefsUtil()

    }

    companion object {
        val instance: PrefsUtil by lazy { Holder.INSTANCE }

        const val TOKEN = "TOKEN"
    }

    fun setStringValue(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun getStringValue(key: String, defaulValue: String): String {
        return prefs.getString(key, defaulValue)
    }

    fun setIntValue(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    fun getIntValue(key: String, defaulValue: Int): Int {
        return prefs.getInt(key, defaulValue)
    }

    fun setBooleanValue(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getBooleanValue(key: String, defaulValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaulValue)
    }

    fun removeValue(key: String) {
        editor.remove(key)
    }

    fun clear() {
        editor.remove(TOKEN)
        editor.commit()
    }

    fun setUserFCMToken(token: String) {
        setStringValue(TOKEN, token)
    }
}

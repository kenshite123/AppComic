package com.ggg.common.utils

import android.database.Cursor

object CursorUtil {
    fun getString(c: Cursor, columnName: String?): String {
        return getString(c, columnName, "", "")
    }

    fun getString(c: Cursor, columnName: String?, defaultValue: String): String {
        return getString(c, columnName, defaultValue, "")
    }

    fun getString(c: Cursor, columnName: String?, defaultValue: String, nullValue: String): String {
        var str = defaultValue
        val columnIndex = c.getColumnIndex(columnName)
        if (columnIndex >= 0) {
            str = if (!c.isNull(columnIndex)) {
                c.getString(columnIndex)
            } else {
                nullValue
            }
        }
        return str
    }

    fun getInt(c: Cursor, columnName: String?): Int {
        return getInt(c, columnName, 0, 0)
    }

    fun getInt(c: Cursor, columnName: String?, defaultValue: Int, nullValue: Int): Int {
        var i = defaultValue
        val columnIndex = c.getColumnIndex(columnName)
        if (columnIndex >= 0) {
            i = if (!c.isNull(columnIndex)) {
                c.getInt(columnIndex)
            } else {
                nullValue
            }
        }
        return i
    }

    fun getInt(c: Cursor, columnName: String?, defaultValue: Int): Int {
        return getInt(c, columnName, defaultValue, 0)
    }

    fun getLong(c: Cursor, columnName: String?): Long {
        return getLong(c, columnName, 0, 0)
    }

    fun getLong(c: Cursor, columnName: String?, defaultValue: Long, nullValue: Long): Long {
        var i = defaultValue
        val columnIndex = c.getColumnIndex(columnName)
        if (columnIndex >= 0) {
            i = if (!c.isNull(columnIndex)) {
                c.getLong(columnIndex)
            } else {
                nullValue
            }
        }
        return i
    }

    fun getLong(c: Cursor, columnName: String?, defaultValue: Long): Long {
        return getLong(c, columnName, defaultValue, 0)
    }

    fun getDouble(c: Cursor, columnName: String?): Double {
        return getDouble(c, columnName, 0.0, 0.0)
    }

    fun getDouble(c: Cursor, columnName: String?, defaultValue: Double): Double {
        return getDouble(c, columnName, defaultValue, 0.0)
    }

    fun getDouble(c: Cursor, columnName: String?, defaultValue: Double, nullValue: Double): Double {
        var i = defaultValue
        val columnIndex = c.getColumnIndex(columnName)
        if (columnIndex >= 0) {
            i = if (!c.isNull(columnIndex)) {
                c.getDouble(columnIndex)
            } else {
                nullValue
            }
        }
        return i
    }

    fun getFloat(c: Cursor, columnName: String?, defaultValue: Float, nullValue: Float): Float {
        var f = defaultValue
        val columnIndex = c.getColumnIndex(columnName)
        if (columnIndex >= 0) {
            f = if (!c.isNull(columnIndex)) {
                c.getFloat(columnIndex)
            } else {
                nullValue
            }
        }
        return f
    }

    fun getFloat(c: Cursor, columnName: String?, defaultValue: Float): Float {
        return getFloat(c, columnName, defaultValue, 0f)
    }

    fun getFloat(c: Cursor, columnName: String?): Float {
        return getFloat(c, columnName, 0f, 0f)
    }
}
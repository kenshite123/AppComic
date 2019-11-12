package com.ggg.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ggg.home.data.local.db.UserDao
import com.ggg.home.data.model.UserModel

@Database(entities = arrayOf(UserModel::class), version = 1, exportSchema = false)
abstract class HomeDB : RoomDatabase() {
    abstract fun userDao(): UserDao
}

package com.ggg.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ggg.home.data.local.db.ComicDao
import com.ggg.home.data.model.ComicModel

@Database(entities = arrayOf(ComicModel::class), version = 1, exportSchema = false)
abstract class HomeDB : RoomDatabase() {
    abstract fun comicDao(): ComicDao
}

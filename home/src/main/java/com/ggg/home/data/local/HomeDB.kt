package com.ggg.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ggg.home.data.local.db.CategoryDao
import com.ggg.home.data.local.db.CategoryOfComicDao
import com.ggg.home.data.local.db.ComicDao
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.CategoryOfComicModel
import com.ggg.home.data.model.ComicModel

@Database(entities = [ComicModel::class, CategoryModel::class, CategoryOfComicModel::class],
        version = 1,
        exportSchema = false)
abstract class HomeDB : RoomDatabase() {
    abstract fun comicDao(): ComicDao
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryOfComicDao(): CategoryOfComicDao
}

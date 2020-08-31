package com.ggg.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ggg.home.data.local.db.*
import com.ggg.home.data.model.*

@Database(entities = [ComicModel::class, CategoryModel::class, CategoryOfComicModel::class,
    ChapterModel::class, CCHadReadModel::class, ComicRankModel::class],
        version = 3,
        exportSchema = false)
abstract class HomeDB : RoomDatabase() {
    abstract fun comicDao(): ComicDao
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryOfComicDao(): CategoryOfComicDao
    abstract fun chapterDao(): ChapterDao
    abstract fun ccHadReadDao(): CCHadReadDao
    abstract fun comicRankDao(): ComicRankDao

    companion object {
        val migration12 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ComicModel ADD COLUMN deploy INTEGER NOT NULL DEFAULT 0 ")
                database.execSQL("ALTER TABLE ComicRankModel ADD COLUMN deploy INTEGER NOT NULL DEFAULT 0 ")
            }
        }

        val migration23 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ChapterModel ADD COLUMN hadDownloaded INTEGER NOT NULL DEFAULT 0 ")
            }
        }
    }
}

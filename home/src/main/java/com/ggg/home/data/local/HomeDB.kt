package com.ggg.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ggg.home.data.local.db.*
import com.ggg.home.data.model.*

@Database(entities = [ComicModel::class, CategoryModel::class, CategoryOfComicModel::class,
    ChapterModel::class, CCHadReadModel::class, CommentModel::class, ReplyModel::class, UserCommentModel::class],
        version = 1,
        exportSchema = false)
abstract class HomeDB : RoomDatabase() {
    abstract fun comicDao(): ComicDao
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryOfComicDao(): CategoryOfComicDao
    abstract fun chapterDao(): ChapterDao
    abstract fun ccHadReadDao(): CCHadReadDao

    companion object {
        val migration12 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ComicModel ADD COLUMN hadFollow INTEGER NOT NULL DEFAULT 0 ")
            }
        }
    }
}

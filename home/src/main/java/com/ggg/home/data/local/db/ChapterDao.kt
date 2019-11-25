package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ChapterModel

@Dao
abstract class ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListChapter(listChapter: List<ChapterModel>)

    @Query("SELECT * FROM ChapterModel WHERE 1 = 1 AND comicId = :comicId ORDER BY chapterId DESC")
    abstract fun getListChaptersComic(comicId: Long): LiveData<List<ChapterModel>>

    @Transaction
    @Query("SELECT chap.* FROM ChapterModel chap LEFT JOIN CCHadReadModel cchrm on chap.chapterId = cchrm.chapterId and chap.comicId = cchrm.comicId WHERE 1 = 1 AND chap.comicId = :comicId ORDER BY chap.chapterId DESC")
    abstract fun getListChaptersComicHadRead(comicId: Long): LiveData<List<ChapterHadRead>>
}
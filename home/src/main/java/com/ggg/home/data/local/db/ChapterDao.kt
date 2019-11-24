package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ggg.home.data.model.ChapterModel

@Dao
abstract class ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListChapter(listComic: List<ChapterModel>)

    @Query("SELECT * FROM ChapterModel WHERE 1 = 1 AND comicId = :comicId ORDER BY chapterId DESC")
    abstract fun getListChaptersComic(comicId: Long): LiveData<List<ChapterModel>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateReadChapterComic(chapterModel: ChapterModel)

    @Query("UPDATE ChapterModel SET isRead = :isRead WHERE 1 = 1 AND chapterId = :chapterId")
    abstract fun updateReadChapterComic(chapterId: Long, isRead: Boolean)
}
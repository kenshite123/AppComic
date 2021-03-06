package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.utils.Constant

@Dao
abstract class ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListChapter(listChapter: List<ChapterModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertChapter(chapterModel: ChapterModel)

    @Query("SELECT * FROM ChapterModel WHERE 1 = 1 AND comicId = :comicId ORDER BY chapterId DESC")
    abstract fun getListChaptersComic(comicId: Long): LiveData<List<ChapterModel>>

    @Query("SELECT * FROM ChapterModel WHERE 1 = 1 AND comicId = :comicId ORDER BY chapterId DESC")
    abstract fun getListChaptersComicData(comicId: Long): List<ChapterModel>

    @Transaction
    @Query("SELECT chap.* FROM ChapterModel chap LEFT JOIN CCHadReadModel cchrm on chap.chapterId = cchrm.chapterId and chap.comicId = cchrm.comicId WHERE 1 = 1 AND chap.comicId = :comicId ORDER BY chap.chapterId DESC")
    abstract fun getListChaptersComicHadRead(comicId: Long): LiveData<List<ChapterHadRead>>

    @Transaction
    @Query("SELECT chap.* FROM ChapterModel chap LEFT JOIN CCHadReadModel cchrm on chap.chapterId = cchrm.chapterId and chap.comicId = cchrm.comicId WHERE 1 = 1 AND chap.chapterId = :chapterId ORDER BY chap.chapterId DESC")
    abstract fun getChapterHadRead(chapterId: Long): LiveData<ChapterHadRead>

    @Query("UPDATE ChapterModel SET hadDownloaded = ${Constant.IS_NOT_DOWNLOAD}, listImageUrlString = '' where 1 = 1")
    abstract fun clearCacheImageDownload()

    @Query("UPDATE ChapterModel SET hadDownloaded = ${Constant.IS_DOWNLOADED} where 1 = 1 and chapterId = :chapterId")
    abstract fun updateChapDownloaded(chapterId: Long)

    @Query("UPDATE ChapterModel SET hadDownloaded = ${Constant.IS_NOT_DOWNLOAD} where 1 = 1 and comicId in (:listComicId)")
    abstract fun updateChapNotDownloaded(listComicId: List<Long>)
}
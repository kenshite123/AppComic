package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ggg.home.data.model.DownloadComicModel
import com.ggg.home.data.model.DownloadComicTotalProgress
import com.ggg.home.utils.Constant

@Dao
abstract class DownloadComicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertDownloadComic(downloadComicModel: DownloadComicModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListDownloadComic(listComic: List<DownloadComicModel>)

    @Query("UPDATE DownloadComicModel SET hadDownloaded = ${Constant.IS_DOWNLOADED} where 1 = 1 and id = :id")
    abstract fun updateDownloadedComic(id: String)

    @Query("SELECT * FROM DownloadComicModel WHERE 1 = 1 AND chapterId = :chapterId")
    abstract fun getListDownloadComic(chapterId: Long) : List<DownloadComicModel>

    @Query("select dcm.comicId, count(case when dcm.hadDownloaded = 1 then 1 else null end) as totalDownloaded, count(id) as totalNeedToDownload from DownloadComicModel dcm where 1 = 1 GROUP BY dcm.comicId limit :limit offset :offset")
    abstract fun getListDownloadComicAndProgress(limit: Int, offset: Int) : List<DownloadComicTotalProgress>

    @Query("select * from DownloadComicModel where 1 = 1 and hadDownloaded = ${Constant.IS_NOT_DOWNLOAD}")
    abstract fun getAllListNotDownloaded() : LiveData<List<DownloadComicModel>>

    @Query("update DownloadComicModel set hadDownloaded = ${Constant.IS_DOWNLOADING} where 1 = 1 and hadDownloaded = ${Constant.IS_NOT_DOWNLOAD}")
    abstract fun updateListNotDownloadToDownloading()

    @Query("update DownloadComicModel set hadDownloaded = ${Constant.IS_NOT_DOWNLOAD} where 1 = 1 and hadDownloaded = ${Constant.IS_DOWNLOADING}")
    abstract fun updateListDownloadingToNotDownload()

    @Query("DELETE FROM DownloadComicModel WHERE 1 = 1")
    abstract fun clearCacheImageDownload()

    @Query("DELETE FROM DownloadComicModel WHERE 1 = 1 AND comicId = :comicId")
    abstract fun clearCacheImageDownload(comicId: Long)

    @Query("DELETE FROM DownloadComicModel WHERE 1 = 1 AND comicId in (:listComicId)")
    abstract fun clearCacheImageDownload(listComicId: List<Long>)
}
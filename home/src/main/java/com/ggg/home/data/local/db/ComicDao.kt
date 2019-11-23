package com.ggg.home.data.local.db

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ggg.home.data.model.ComicModel

@Dao
abstract class ComicDao {
//    @Query("")
//    abstract fun getListComicLatestUpdate()

    @Query("SELECT * FROM ComicModel where 1 = 1 and (bigImageUrl IS NOT NULL and bigImageUrl <> '')")
    abstract fun getListBanners() : LiveData<List<ComicModel>>

    @Query("SELECT * FROM ComicModel where 1 = 1 and (latestChapter IS NOT NULL and latestChapter <> '') ORDER BY id DESC limit :limit offset :offset")
    abstract fun getListLatestUpdate(limit: Int, offset: Int) : LiveData<List<ComicModel>>

    @Query("SELECT * FROM ComicModel where 1 = 1 and categoriesString like '%' || :categoryId || '%' ORDER BY id DESC limit :limit offset :offset")
    abstract fun getListComicByCategory(categoryId: Long, limit: Int, offset: Int) : LiveData<List<ComicModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListComic(listComic: List<ComicModel>)

    @Query("UPDATE ComicModel SET bigImageUrl = '' where 1 = 1 and (bigImageUrl IS NOT NULL OR bigImageUrl <> '')")
    abstract fun updateClearListBanners()
}
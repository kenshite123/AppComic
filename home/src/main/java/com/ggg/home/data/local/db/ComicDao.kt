package com.ggg.home.data.local.db

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

    @Query("SELECT * FROM ComicModel where 1 = 1 and (bigImageUrl IS NOT NULL OR bigImageUrl <> '')")
    abstract fun getListBanners() : LiveData<List<ComicModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListBanners(listBanners: List<ComicModel>)

    @Query("DELETE FROM ComicModel where 1 = 1 and (bigImageUrl IS NOT NULL OR bigImageUrl <> '')")
    abstract fun deleteListBanners()
}
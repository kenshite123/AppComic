package com.ggg.home.data.local.db

import androidx.room.*
import com.ggg.home.data.model.CCHadReadModel

@Dao
abstract class CCHadReadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCCHadRead(ccHadRead: CCHadReadModel) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListCCHadRead(listCCHadRead: List<CCHadReadModel>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateCCHadRead(ccHadRead: CCHadReadModel)

    @Query("DELETE FROM CCHadReadModel WHERE 1 = 1 AND comicId in (:listComicId)")
    abstract fun deleteListHistory(listComicId: List<Long>)
}
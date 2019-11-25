package com.ggg.home.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.ggg.home.data.model.CCHadReadModel

@Dao
abstract class CCHadReadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCCHadRead(ccHadRead: CCHadReadModel) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListCCHadRead(listCCHadRead: List<CCHadReadModel>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateCCHadRead(ccHadRead: CCHadReadModel)
}
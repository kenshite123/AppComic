package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ggg.home.data.model.CategoryModel

@Dao
abstract class CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListCategories(listCategories: List<CategoryModel>)

    @Query("SELECT * FROM CategoryModel WHERE 1 = 1 AND status = 1")
    abstract fun getAllListCategories() : LiveData<List<CategoryModel>>

//    @Query("SELECT * FROM CategoryModel WHERE 1 = 1 AND comicId = :comicId")
//    abstract fun getListCategoriesByComicId(comicId: Long) : LiveData<List<CategoryModel>>
}
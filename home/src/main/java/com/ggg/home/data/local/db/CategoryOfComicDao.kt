package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ggg.home.data.model.CategoryOfComicModel

@Dao
abstract class CategoryOfComicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListCategoriesOfComic(listCategoriesOfComic: List<CategoryOfComicModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCategoryOfComic(categoryOfComic: CategoryOfComicModel)

    @Query("SELECT * FROM CategoryOfComicModel WHERE 1 = 1 AND comicId = :comicId")
    abstract fun getListCategoriesOfComic(comicId: Long) : LiveData<List<CategoryOfComicModel>>
}
package com.ggg.home.data.local.db

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel

@Dao
abstract class ComicDao {
//    @Query("")
//    abstract fun getListComicLatestUpdate()

    @Query("SELECT comic.*, group_concat(cate.categoryId) as groupCateId, group_concat(cate.categoryName) as groupCateName  FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.bigImageUrl IS NOT NULL and bigImageUrl <> '') GROUP BY comic.id")
    abstract fun getListBanners() : LiveData<List<ComicWithCategoryModel>>

    @Query("SELECT comic.*, group_concat(cate.categoryId) as groupCateId, group_concat(cate.categoryName) as groupCateName FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') GROUP BY comic.id ORDER BY id DESC limit :limit offset :offset")
    abstract fun getListLatestUpdate(limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>
//    abstract fun getListLatestUpdate() : LiveData<List<ComicWithCategoryModel>>

    @Query("SELECT comic.*, group_concat(cate.categoryId) as groupCateId, group_concat(cate.categoryName) as groupCateName  FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and cate.categoryId = :categoryId GROUP BY comic.id ORDER BY comic.id DESC limit :limit offset :offset")
    abstract fun getListComicByCategory(categoryId: Long, limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListComic(listComic: List<ComicModel>)

    @Query("UPDATE ComicModel SET bigImageUrl = '' where 1 = 1 and (bigImageUrl IS NOT NULL OR bigImageUrl <> '')")
    abstract fun updateClearListBanners()
}
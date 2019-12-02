package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel

@Dao
abstract class ComicDao {
    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.bigImageUrl IS NOT NULL and bigImageUrl <> '') GROUP BY comic.id")
    abstract fun getListBanners() : LiveData<MutableList<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') GROUP BY comic.id ORDER BY lastModified DESC limit :limit offset :offset")
    abstract fun getListLatestUpdate(limit: Int, offset: Int) : LiveData<MutableList<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and cate.categoryId = :categoryId and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') GROUP BY comic.id ORDER BY comic.id DESC limit :limit offset :offset")
    abstract fun getListComicByCategory(categoryId: Long, limit: Int, offset: Int) : LiveData<MutableList<ComicWithCategoryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListComic(listComic: List<ComicModel>)

    @Query("UPDATE ComicModel SET bigImageUrl = '' where 1 = 1 and (bigImageUrl IS NOT NULL AND bigImageUrl <> '')")
    abstract fun updateClearListBanners()

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') GROUP BY comic.id ORDER BY comic.rate, comic.lastModified DESC limit :limit offset :offset")
    abstract fun getListFavoriteComic(limit: Int, offset: Int) : LiveData<MutableList<ComicWithCategoryModel>>
}
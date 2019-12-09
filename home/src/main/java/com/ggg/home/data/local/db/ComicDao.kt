package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.HistoryModel

@Dao
abstract class ComicDao {
    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.bigImageUrl IS NOT NULL and bigImageUrl <> '') GROUP BY comic.id")
    abstract fun getListBanners() : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') GROUP BY comic.id ORDER BY comic.lastModified DESC limit :limit offset :offset")
    abstract fun getListLatestUpdate(limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and cate.categoryId = :categoryId and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') GROUP BY comic.id ORDER BY comic.id DESC limit :limit offset :offset")
    abstract fun getListComicByCategory(categoryId: Long, limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertComic(comic: ComicModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListComic(listComic: List<ComicModel>)

    @Query("UPDATE ComicModel SET bigImageUrl = '' where 1 = 1 and (bigImageUrl IS NOT NULL AND bigImageUrl <> '')")
    abstract fun updateClearListBanners()

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') GROUP BY comic.id ORDER BY comic.rate, comic.lastModified DESC limit :limit offset :offset")
    abstract fun getListFavoriteComic(limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and comic.id = :comicId")
    abstract fun getComicInfo(comicId: Long) : LiveData<ComicWithCategoryModel>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CCHadReadModel ccHadRead on comic.id = ccHadRead.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') GROUP BY comic.id ORDER BY ccHadRead.lastModified DESC limit :limit offset :offset")
    abstract fun getListHistory(limit: Int, offset: Int) : LiveData<List<HistoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 /*and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '')*/ and comic.id in (:data) GROUP BY comic.id ORDER BY comic.lastModified DESC")
    abstract fun getListComicFollow(data: List<String>) : LiveData<List<ComicWithCategoryModel>>
}
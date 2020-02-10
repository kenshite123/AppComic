package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.HistoryModel

@Dao
abstract class ComicDao {
    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.bigImageUrl IS NOT NULL and bigImageUrl <> '') and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id")
    abstract fun getListBanners(siteDeploy: Boolean) : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY comic.lastModified DESC limit :limit offset :offset")
    abstract fun getListLatestUpdate(siteDeploy: Boolean, limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and cate.categoryId in (:listCategoryId) and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY comic.id DESC limit :limit offset :offset")
    abstract fun getListComicByCategory(siteDeploy: Boolean, listCategoryId: List<Long>, limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertComic(comic: ComicModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListComic(listComic: List<ComicModel>)

    @Query("UPDATE ComicModel SET bigImageUrl = '' where 1 = 1 and (bigImageUrl IS NOT NULL AND bigImageUrl <> '')")
    abstract fun updateClearListBanners()

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY comic.rate, comic.lastModified DESC limit :limit offset :offset")
    abstract fun getListFavoriteComic(siteDeploy: Boolean, limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and comic.id = :comicId")
    abstract fun getComicInfo(comicId: Long) : LiveData<ComicWithCategoryModel>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CCHadReadModel ccHadRead on comic.id = ccHadRead.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY ccHadRead.lastModified DESC limit :limit offset :offset")
    abstract fun getListHistory(siteDeploy: Boolean, limit: Int, offset: Int) : LiveData<List<HistoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 /*and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '')*/ and comic.id in (:data) and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY comic.lastModified DESC")
    abstract fun getListComicFollow(siteDeploy: Boolean, data: List<String>) : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and cate.categoryId in (:listCategoryId) and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and (case :status when 'All' then  comic.status in (0,1) when 'Updating' then  comic.status = 0 else comic.status = 1  end) and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY (case :type  when 'New' then comic.id else comic.viewed end) DESC limit :limit offset :offset")
    abstract fun getAllListComic(siteDeploy: Boolean, listCategoryId: List<Long>, status: String, type: String, limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and (case :status when 'All' then  comic.status in (0,1) when 'Updating' then  comic.status = 0 else comic.status = 1  end) GROUP BY comic.id ORDER BY (case :type  when 'New' then comic.id else comic.viewed end) and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) DESC limit :limit offset :offset")
    abstract fun getAllListComic(siteDeploy: Boolean, status: String, type: String, limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and cate.categoryId in (:listCategoryId) and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and (case :status when 'All' then  comic.status in (0,1) when 'Updating' then  comic.status = 0 else comic.status = 1  end) and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY comic.lastModified DESC limit :limit offset :offset")
    abstract fun getListLatestUpdateByFilter(siteDeploy: Boolean, listCategoryId: List<Long>, status: String, limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and (case :status when 'All' then  comic.status in (0,1) when 'Updating' then  comic.status = 0 else comic.status = 1  end) and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY comic.lastModified DESC limit :limit offset :offset")
    abstract fun getListLatestUpdateByFilter(siteDeploy: Boolean, status: String, limit: Int, offset: Int) : LiveData<List<ComicWithCategoryModel>>
}
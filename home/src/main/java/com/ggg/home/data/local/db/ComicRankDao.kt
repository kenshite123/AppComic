package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ggg.home.data.model.ComicRankModel
import com.ggg.home.data.model.ComicRankWithCategoryModel

@Dao
abstract class ComicRankDao {
    @Transaction
    @Query("SELECT comic.* FROM ComicRankModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and comic.type = :type and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY lastModified ASC limit :limit offset :offset")
    abstract fun getListRankByType(siteDeploy: String, type: String, limit: Int, offset: Int) : LiveData<List<ComicRankWithCategoryModel>>

    @Transaction
    @Query("SELECT comic.* FROM ComicRankModel comic join CategoryOfComicModel cate on comic.id = cate.comicId where 1 = 1 and (comic.latestChapter IS NOT NULL and comic.latestChapter <> '') and comic.type = :type and (case :siteDeploy when 'false' then  comic.deploy in (0,1) else comic.deploy = 0  end) GROUP BY comic.id ORDER BY lastModified ASC limit :limit offset :offset")
    abstract fun getListRankByType(siteDeploy: Boolean, type: String, limit: Int, offset: Int) : LiveData<List<ComicRankWithCategoryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListComic(listComic: List<ComicRankModel>)

    @Query("DELETE FROM ComicRankModel WHERE 1 = 1 AND type = :type")
    abstract fun deleteListComicRankByType(type: String)
}
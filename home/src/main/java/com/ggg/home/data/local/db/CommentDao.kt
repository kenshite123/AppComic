package com.ggg.home.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ggg.home.data.model.CommentModel

@Dao
abstract class CommentDao {
    @Transaction
    @Query("SELECT * FROM CommentModel cmt WHERE 1 = 1 AND cmt.comicId = :comicId GROUP BY cmt.commentId ORDER BY cmt.createdAt DESC LIMIT :limit OFFSET :offset")
    abstract fun getListComments(comicId: Long, limit: Long, offset: Long) : LiveData<List<CommentModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListComments(listComments : List<CommentModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertComment(commentModel : CommentModel)
}
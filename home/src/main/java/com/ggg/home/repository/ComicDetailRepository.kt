package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkBoundResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.*
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import org.jetbrains.anko.doAsync
import org.w3c.dom.Text
import javax.inject.Inject

class ComicDetailRepository {
    private val executor: AppExecutors
    private var api: HomeService
    private var retrofit: HomeRetrofitProvider
    private var db: HomeDB

    @Inject
    constructor(appExec: AppExecutors, retrofit: HomeRetrofitProvider, db: HomeDB) {
        this.executor = appExec
        api = retrofit.connectAPI()
        this.retrofit = retrofit
        this.db = db
    }

    fun getListChapters(comicId: Long): LiveData<Resource<List<ChapterHadRead>>> {
        val callApi = object : NetworkBoundResource<List<ChapterHadRead>, List<ChapterModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ChapterHadRead>> {
                return db.chapterDao().getListChaptersComicHadRead(comicId)
            }

            override fun createCall(): LiveData<ApiResponse<List<ChapterModel>>> {
                return api.getListChaptersComic(comicId)
            }

            override fun saveCallResult(item: List<ChapterModel>) {
                if (item.isNotEmpty()) {
                    item.map {
                        it.listImageUrlString = TextUtils.join(", ", it.imageUrls)
                        it.comicId = comicId
                        it.lastModified = System.currentTimeMillis()
                    }
                    db.chapterDao().insertListChapter(item)
                }
            }

            override fun shouldFetch(data: List<ChapterHadRead>?): Boolean {
                return true
            }
        }
        return callApi.asLiveData()
    }

    fun insertCCHadRead(ccHadReadModel: CCHadReadModel) {
        doAsync {
            db.ccHadReadDao().insertCCHadRead(ccHadReadModel)
        }
    }

    fun getListComments(data: HashMap<String, Long>): LiveData<Resource<List<CommentModel>>> {
        val callApi = object : NetworkBoundResource<List<CommentModel>, List<CommentModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<CommentModel>> {
                return db.commentDao().getListComments(
                        data["comicId"]!!,
                        data["limit"]!!,
                        data["offset"]!!
                )
            }

            override fun createCall(): LiveData<ApiResponse<List<CommentModel>>> {
                return api.getListCommentByComic(
                        data["comicId"]!!,
                        data["limit"]!!,
                        data["offset"]!!
                )
            }

            override fun saveCallResult(item: List<CommentModel>) {
                if (item.isNotEmpty()) {
                    item.forEach {
                        it.replies.forEach { reply ->
                            reply.commentParentId = it.commentId
                            db.commentDao().insertComment(reply)
                        }
                    }
                    db.commentDao().insertListComments(item)
                }
            }

            override fun shouldFetch(data: List<CommentModel>?): Boolean {
                return true
            }
        }
        return callApi.asLiveData()
    }

    fun getComicInfo(comicId: Long): Nothing {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
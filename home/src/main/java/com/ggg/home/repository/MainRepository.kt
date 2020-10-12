package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyDbResource
import com.ggg.common.utils.Utils
import com.ggg.common.vo.Resource
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.DownloadComicModel
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import com.ggg.home.utils.Constant
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class MainRepository {
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

    fun updateDownloadedComic(srcImg: String) {
        doAsync {
            db.downloadComicDao().updateDownloadedComic(id = Utils.md5(srcImg))
        }
    }

    fun updateChapDownloaded(chapterId: Long) {
        doAsync {
            db.chapterDao().updateChapDownloaded(chapterId = chapterId)
        }
    }

    fun updateListDownloadingToNotDownload() {
        doAsync {
            db.downloadComicDao().updateListDownloadingToNotDownload()
        }
    }
}
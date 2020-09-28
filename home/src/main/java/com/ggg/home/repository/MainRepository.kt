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

    fun updateDownloadedComic(comicId: Long, srcImg: String, chapterId: Long) {
        doAsync {
            db.downloadComicDao().updateDownloadedComic(id = Utils.md5(srcImg))
            val listDownloadComic = db.downloadComicDao().getListDownloadComic(chapterId = chapterId)
            if (!listDownloadComic.isNullOrEmpty()) {
                val totalDownload = listDownloadComic.count()
                var totalDownloaded = 0
                listDownloadComic.forEach {
                    if (it.hadDownloaded == Constant.IS_DOWNLOADED) {
                        totalDownloaded++
                    }
                }

                if (totalDownloaded == totalDownload) {
                    db.chapterDao().updateChapDownloaded(chapterId = chapterId)
//                    db.downloadComicDao().clearCacheImageDownload(comicId = comicId)
                    GGGAppInterface.gggApp.bus().sendDownloadImageDone(comicId = comicId)
                }
            }
        }
    }

    fun updateDownloadComic(downloadComicModel: DownloadComicModel) {
        doAsync {
            db.downloadComicDao().insertDownloadComic(downloadComicModel = downloadComicModel)
        }
    }

    fun getListComicNotDownloaded(): LiveData<Resource<List<DownloadComicModel>>> {
        val getDataFromDb = object : NetworkOnlyDbResource<List<DownloadComicModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<DownloadComicModel>> {
                return db.downloadComicDao().getAllListNotDownloaded()
            }
        }
        return getDataFromDb.asLiveData()
    }
}
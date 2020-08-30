package com.ggg.home.ui.view_comic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.CCHadReadModel
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.post_param.DataSendReportParam
import com.ggg.home.repository.UserRepository
import com.ggg.home.repository.ViewComicRepository
import javax.inject.Inject

class ViewComicViewModel @Inject constructor(private val viewComicRepository: ViewComicRepository) : ViewModel() {
    private val requestGetListImage: MutableLiveData<ChapterHadRead> = MutableLiveData()
    var getListImageResult: LiveData<Resource<ChapterHadRead>>

    private val requestSendReport: MutableLiveData<HashMap<String, Any?>> = MutableLiveData()
    var sendReportResult: LiveData<Resource<Any>>

    init {
        getListImageResult = Transformations.switchMap(requestGetListImage) {
            return@switchMap viewComicRepository.getListImage(it)
        }

        sendReportResult = Transformations.switchMap(requestSendReport) {
            return@switchMap viewComicRepository.sendReport(it)
        }
    }

    fun insertCCHadRead(ccHadReadModel: CCHadReadModel) {
        viewComicRepository.insertCCHadRead(ccHadReadModel)
    }

    fun getListImageOfChapter(chapterHadRead: ChapterHadRead) {
        requestGetListImage.value = chapterHadRead
    }

    fun sendReport(data: HashMap<String, Any?>) {
        requestSendReport.value = data
    }
}
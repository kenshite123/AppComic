package com.ggg.home.ui.comment_of_chap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.CommentModel
import com.ggg.home.data.model.response.CommentResponse
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.repository.CommentOfChapRepository
import javax.inject.Inject

class CommentOfChapViewModel @Inject constructor(private val commentOfChapRepository: CommentOfChapRepository) : ViewModel() {
    private val requestGetListCommentOfChap: MutableLiveData<HashMap<String, Long>> = MutableLiveData()
    var getListCommentOfChapResult: LiveData<Resource<List<CommentModel>>>

    private val requestWriteComment: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var writeCommentResult: LiveData<Resource<CommentResponse>>

    init {
        getListCommentOfChapResult = Transformations.switchMap(requestGetListCommentOfChap) {
            return@switchMap commentOfChapRepository.getListCommentOfChap(it)
        }

        writeCommentResult = Transformations.switchMap(requestWriteComment) {
            return@switchMap commentOfChapRepository.writeComment(it)
        }
    }

    fun getListCommentOfChap(data: HashMap<String, Long>) {
        requestGetListCommentOfChap.value = data
    }

    fun writeComment(data: HashMap<String, Any>) {
        requestWriteComment.value = data
    }
}
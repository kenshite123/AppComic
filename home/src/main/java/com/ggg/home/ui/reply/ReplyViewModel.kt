package com.ggg.home.ui.reply

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.repository.ReplyRepository
import javax.inject.Inject

class ReplyViewModel @Inject constructor(private val replyRepository: ReplyRepository) : ViewModel() {
    private val requestWriteComment: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var writeCommentResult: LiveData<Resource<NoneResponse>>

    init {
        writeCommentResult = Transformations.switchMap(requestWriteComment) {
            return@switchMap replyRepository.writeComment(it)
        }
    }

    fun writeComment(data: HashMap<String, Any>) {
        requestWriteComment.value = data
    }
}
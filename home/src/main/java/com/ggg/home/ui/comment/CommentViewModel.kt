package com.ggg.home.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.response.CommentResponse
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.repository.CommentRepository
import javax.inject.Inject

class CommentViewModel @Inject constructor(private val commentRepository: CommentRepository) : ViewModel() {
    private val requestWriteComment: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var writeCommentResult: LiveData<Resource<CommentResponse>>

    init {
        writeCommentResult = Transformations.switchMap(requestWriteComment) {
            return@switchMap commentRepository.writeComment(it)
        }
    }

    fun writeComment(data: HashMap<String, Any>) {
        requestWriteComment.value = data
    }
}
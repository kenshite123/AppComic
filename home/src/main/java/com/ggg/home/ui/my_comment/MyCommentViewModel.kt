package com.ggg.home.ui.my_comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.CommentModel
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.repository.MyCommentRepository
import javax.inject.Inject

class MyCommentViewModel @Inject constructor(private val myCommentRepository: MyCommentRepository) : ViewModel() {
    private val requestGetListMyComment: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var getListMyCommentResult: LiveData<Resource<List<CommentModel>>>

    private val requestDeleteComment: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var deleteCommentResult: LiveData<Resource<List<NoneResponse>>>

    init {
        getListMyCommentResult = Transformations.switchMap(requestGetListMyComment) {
            return@switchMap myCommentRepository.getListMyComment(it)
        }

        deleteCommentResult = Transformations.switchMap(requestDeleteComment) {
            return@switchMap myCommentRepository.deleteComment(it)
        }
    }

    fun getListMyComment(data: HashMap<String, Any>) {
        requestGetListMyComment.value = data
    }

    fun deleteComment(data: HashMap<String, Any>) {
        requestDeleteComment.value = data
    }
}
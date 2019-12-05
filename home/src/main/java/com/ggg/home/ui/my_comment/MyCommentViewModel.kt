package com.ggg.home.ui.my_comment

import androidx.lifecycle.ViewModel
import com.ggg.home.repository.MyCommentRepository
import javax.inject.Inject

class MyCommentViewModel @Inject constructor(private val myCommentRepository: MyCommentRepository) : ViewModel() {
    init {

    }
}
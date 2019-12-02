package com.ggg.home.ui.comment

import androidx.lifecycle.ViewModel
import com.ggg.home.repository.CommentRepository
import javax.inject.Inject

class CommentViewModel @Inject constructor(private val commentRepository: CommentRepository) : ViewModel() {
    init {

    }
}
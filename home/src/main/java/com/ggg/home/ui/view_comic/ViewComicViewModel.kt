package com.ggg.home.ui.view_comic

import androidx.lifecycle.ViewModel
import com.ggg.home.data.model.CCHadReadModel
import com.ggg.home.repository.UserRepository
import com.ggg.home.repository.ViewComicRepository
import javax.inject.Inject

class ViewComicViewModel @Inject constructor(private val viewComicRepository: ViewComicRepository) : ViewModel() {
    init {

    }

    fun insertCCHadRead(ccHadReadModel: CCHadReadModel) {
        viewComicRepository.insertCCHadRead(ccHadReadModel)
    }
}
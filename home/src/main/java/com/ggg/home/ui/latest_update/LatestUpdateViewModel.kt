package com.ggg.home.ui.latest_update

import androidx.lifecycle.ViewModel
import com.ggg.home.repository.LatestUpdateRepository
import javax.inject.Inject

class LatestUpdateViewModel @Inject constructor(private val latestUpdateRepository: LatestUpdateRepository) : ViewModel() {
    init {

    }
}
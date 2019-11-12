package com.ggg.home.ui.home

import androidx.lifecycle.ViewModel
import com.ggg.home.repository.HomeRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {
    init {

    }
}
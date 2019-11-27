package com.ggg.home.ui.favorite

import androidx.lifecycle.ViewModel
import com.ggg.home.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    init {

    }
}
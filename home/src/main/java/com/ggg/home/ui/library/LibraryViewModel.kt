package com.ggg.home.ui.library

import androidx.lifecycle.ViewModel
import com.ggg.home.repository.LibraryRepository
import javax.inject.Inject

class LibraryViewModel @Inject constructor(private val libraryRepository: LibraryRepository) : ViewModel() {
    init {

    }
}
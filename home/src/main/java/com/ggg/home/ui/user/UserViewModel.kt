package com.ggg.home.ui.user

import androidx.lifecycle.ViewModel
import com.ggg.home.repository.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    init {

    }
}
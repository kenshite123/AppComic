package com.ggg.home.ui.login.login

import androidx.lifecycle.ViewModel
import com.ggg.home.repository.HomeRepository
import com.ggg.home.repository.LoginRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {
    init {

    }
}
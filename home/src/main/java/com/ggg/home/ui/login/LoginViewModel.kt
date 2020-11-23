package com.ggg.home.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.repository.LoginRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {
    private val requestLoginData: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    var loginResult: LiveData<Resource<LoginResponse>>

    init {
        loginResult = Transformations.switchMap(requestLoginData) {
            return@switchMap loginRepository.login(it)
        }
    }

    fun requestLogin(data: HashMap<String, String>) {
        requestLoginData.value = data
    }
}
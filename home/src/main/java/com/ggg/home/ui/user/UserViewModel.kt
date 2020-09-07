package com.ggg.home.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.repository.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val requestLogOut: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    var logOutResult: LiveData<Resource<NoneResponse>>

    init {
        logOutResult = Transformations.switchMap(requestLogOut) {
            return@switchMap  userRepository.logOut(it)
        }
    }

    fun logOut(param: HashMap<String, String>) {
        requestLogOut.value = param
    }

    fun clearCacheImageDownload() {
        userRepository.clearCacheImageDownload()
    }
}
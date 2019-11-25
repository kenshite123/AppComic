package com.ggg.home.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.response.RegisterResponse
import com.ggg.home.repository.RegisterRepository
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) : ViewModel() {
    private val requestRegisterData: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    var registerResult: LiveData<Resource<RegisterResponse>>

    init {
        registerResult = Transformations.switchMap(requestRegisterData) {
            return@switchMap registerRepository.register(it)
        }
    }

    fun requestRegister(data: HashMap<String, String>) {
        requestRegisterData.value = data
    }
}
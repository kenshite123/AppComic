package com.ggg.home.ui.change_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.response.ChangePassWordResponse
import com.ggg.home.repository.ChangePassWordRepository
import javax.inject.Inject

class ChangePassWordViewModel @Inject constructor(private val changePassWordRepository: ChangePassWordRepository): ViewModel() {
    private var id: Long = 0
    private val requestChangePassWord: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    var changePassWordResult: LiveData<Resource<ChangePassWordResponse>>

    init {
        changePassWordResult = Transformations.switchMap(requestChangePassWord) {
            return@switchMap changePassWordRepository.changePassWord(id, param = it)
        }
    }

    fun changePassWord(userId: Long, param: HashMap<String, String>) {
        id = userId
        requestChangePassWord.value = param
    }
}
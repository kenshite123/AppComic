package com.ggg.app.ui.init

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class InitViewModel @Inject constructor(private val initRepository: InitRepository) : ViewModel() {
    private val dataQuery = MutableLiveData<Boolean>()
    val dataObs = Transformations.switchMap(dataQuery){initRepository.initData()}

    fun getData(){
        dataQuery.value = true
    }
}
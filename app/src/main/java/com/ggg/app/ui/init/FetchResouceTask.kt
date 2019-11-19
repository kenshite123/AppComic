package com.ggg.app.ui.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ggg.common.vo.Resource
import com.ggg.home.data.remote.HomeService

class FetchResouceTask constructor(private val service: HomeService) : Runnable {

    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    override fun run() {
//        try {
//            _liveData.postValue(Resource.loading(null))
//            val categories = service.getCategoriesCall().execute()
//            val apiCategories = ApiResponse(categories)
//            if (!apiCategories.isSuccessful()) {
//                _liveData.postValue(Resource.error(apiCategories.errorMessage!!, false))
//                return
//            }
//
////        val allStatusResponse = service.getAllStatus().execute()
//            val areas = service.getAreas().execute()
//            val yearOfExp = service.getYearOfExp().execute()
//            val apiAreas = ApiResponse(areas)
//
//            val bonusTypeCall = service.getBonusType().execute()
//            val salaryTypeCall = service.getSalaryType().execute()
//            val bonusTypes = ApiResponse(bonusTypeCall)
//            val salaryTypes = ApiResponse(salaryTypeCall)
////        val apiAllStatus = ApiResponse(allStatusResponse)
//
//
//            val yearOfExpdata = ApiResponse(yearOfExp)
//            if (!apiAreas.isSuccessful()
//                    || !yearOfExpdata.isSuccessful()
//                    || !bonusTypes.isSuccessful()
//                    || !salaryTypes.isSuccessful()) {
//                _liveData.postValue(Resource.error("ErrorResponse", false))
//                return
//            }
//
//            if (apiCategories.body!!.data != null
//                    && apiAreas.body!!.data != null
//                    && yearOfExpdata.body!!.data !== null
//                    && bonusTypes.body!!.data != null
//                    && salaryTypes.body!!.data != null) {
//
//                GlobalInfo.instance.isInited = true
//                GlobalInfo.instance.categories = apiCategories.body!!.data!!
//                GlobalInfo.instance.areas = areas.body()!!.data!!
//                GlobalInfo.instance.yearOfExp = yearOfExpdata.body!!.data!!
//                GlobalInfo.instance.bonusTypes = bonusTypes.body!!.data!!
//                GlobalInfo.instance.salaryTypes = salaryTypes.body!!.data!!
//
//                _liveData.postValue(Resource.success(true))
//                return
//            }
//
//            _liveData.postValue(Resource.error("Unknown error", null))
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//            _liveData.postValue(Resource.error("Unknown error", null))
//        }
    }
}
package com.ggg.home.data.remote

import androidx.lifecycle.LiveData
import com.ggg.common.ws.ApiResponse
import com.ggg.common.ws.BaseResponse
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.post_param.RegisterBody
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.data.model.response.RegisterResponse
import com.ggg.home.utils.ServerPath
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeService {
    @GET(ServerPath.LOGIN)
    fun login(
            @Query("username") username: String,
            @Query("password") password: String,
            @Query("token") token: String
    ): LiveData<ApiResponse<LoginResponse>>

    @POST(ServerPath.REGISTER)
    fun register(
            @Body registerBody: RegisterBody
    ): LiveData<ApiResponse<RegisterResponse>>

    @GET(ServerPath.BANNERS)
    fun getBanners() : LiveData<ApiResponse<List<ComicModel>>>

    @GET(ServerPath.LATEST_UPDATE)
    fun getLatestUpdate(
            @Query("items") limit: Int,
            @Query("page") offset: Int
    ) : LiveData<ApiResponse<List<ComicModel>>>

    @GET(ServerPath.LIST_CATEGORIES)
    fun getAllListCategories() : LiveData<ApiResponse<List<CategoryModel>>>

    @GET(ServerPath.LIST_COMIC)
    fun getListComicByCategory(
            @Query("category") categoryId: Long,
            @Query("items") limit: Int,
            @Query("page") offset: Int
    ) : LiveData<ApiResponse<List<ComicModel>>>
}
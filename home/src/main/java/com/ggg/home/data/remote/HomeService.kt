package com.ggg.home.data.remote

import androidx.lifecycle.LiveData
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.model.*
import com.ggg.home.data.model.post_param.RegisterBody
import com.ggg.home.data.model.post_param.WriteCommentBody
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.CommentModel
import com.ggg.home.data.model.post_param.ChangePassWordBody
import com.ggg.home.data.model.post_param.DataGetListImageToDownloadParam
import com.ggg.home.data.model.response.*
import com.ggg.home.utils.ServerPath
import retrofit2.http.*

interface HomeService {
    @GET(ServerPath.LOGIN)
    fun login(
            @Query("username") username: String,
            @Query("password") password: String,
            @Query("token") token: String
    ): LiveData<ApiResponse<LoginResponse>>

    @GET(ServerPath.CONFIG)
    fun getConfig() : LiveData<ApiResponse<ConfigModel>>

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
            @Query("category") listCategoryId: List<Long>,
            @Query("items") limit: Int,
            @Query("page") offset: Int
    ) : LiveData<ApiResponse<List<ComicModel>>>

    @GET(ServerPath.LIST_COMIC)
    fun getListComicByKeyWords(
            @Query("keywords") keywords: String,
            @Query("items") limit: Int,
            @Query("page") offset: Int
    ) : LiveData<ApiResponse<List<ComicModel>>>

    @GET(ServerPath.LIST_CHAPTERS)
    fun getListChaptersComic(
            @Path("comicId") comicId: Long
    ) : LiveData<ApiResponse<List<ChapterModel>>>

    @GET(ServerPath.LIST_COMMENT_BY_COMIC)
    fun getListCommentByComic(
            @Path("comicId") comicId: Long,
            @Query("items") limit: Long,
            @Query("page") offset: Long
    ) : LiveData<ApiResponse<List<CommentModel>>>

    @GET(ServerPath.LIST_COMIC_RANKING)
    fun getListComicRanking(
            @Query("type") type: String,
            @Query("items") limit: Int,
            @Query("page") offset: Int
    ) : LiveData<ApiResponse<List<ComicRankModel>>>

    @PUT(ServerPath.CHANGE_PASSWORD)
    fun changePassword(
            @Header("Authorization") accessToken: String,
            @Path("id") id: Int,
            @Body changePasswordBody: ChangePassWordBody
    ): LiveData<ApiResponse<ChangePassWordResponse>>

    @POST(ServerPath.LOG_OUT)
    fun logOut(
            @Header("Authorization") token: String
    ) : LiveData<ApiResponse<NoneResponse>>

    @GET(ServerPath.LIST_COMIC_FAVORITE)
    fun getListFavoriteComic(
            @Query("items") limit: Int,
            @Query("page") offset: Int
    ) : LiveData<ApiResponse<List<ComicModel>>>

    @POST(ServerPath.WRITE_COMMENT)
    fun writeComment(
            @Header("Authorization") authorization: String,
            @Body writeCommentBody: WriteCommentBody
    ) : LiveData<ApiResponse<CommentResponse>>

    @GET(ServerPath.COMIC_INFO)
    fun getComicInfo(
            @Path("comicId") comicId: Long
    ) : LiveData<ApiResponse<ComicModel>>

    @POST(ServerPath.FAVORITE_COMIC)
    fun favoriteComic(
            @Header("Authorization") accessToken: String,
            @Path("comicId") comicId: Long
    ) : LiveData<ApiResponse<NoneResponse>>

    @PATCH(ServerPath.UNFAVORITE_COMIC)
    fun unFavoriteComic(
            @Header("Authorization") accessToken: String,
            @Path("comicId") comicId: Long
    ) : LiveData<ApiResponse<NoneResponse>>

    @GET(ServerPath.LIST_COMIC_FOLLOW)
    fun getListComicFollow(
            @Header("Authorization") authorization: String,
            @Query("items") limit: Int,
            @Query("page") offset: Int
    ) : LiveData<ApiResponse<List<ComicModel>>>

    @GET(ServerPath.GET_LIST_MY_COMMENT)
    fun getListMyComment(
            @Header("Authorization") authorization: String,
            @Query("items") limit: Int,
            @Query("page") offset: Int
    ) : LiveData<ApiResponse<List<CommentModel>>>

    @PATCH(ServerPath.DELETE_COMMENT)
    fun deleteComment(
            @Header("Authorization") authorization: String,
            @Path("commentId") commentId: Long
    ) : LiveData<ApiResponse<List<NoneResponse>>>

    @POST(ServerPath.GET_COMMENT_DETAIL)
    fun getCommentDetail(
            @Path("commentId") commentId: Long
    ) : LiveData<ApiResponse<CommentModel>>

    @GET(ServerPath.GET_LIST_COMMENT_OF_CHAP)
    fun getListCommentOfChap(
            @Path("comicId") comicId: Long,
            @Path("chapterId") chapterId: Long,
            @Query("items") items: Long,
            @Query("page") page: Long
    ) : LiveData<ApiResponse<List<CommentModel>>>

    @GET(ServerPath.GET_LIST_IMAGE_OF_CHAP)
    fun getListImageOfChap(
            @Path("comicId") comicId: Long,
            @Path("chapterId") chapterId: Long
    ) : LiveData<ApiResponse<List<String>>>

    @GET(ServerPath.LIST_COMIC)
    fun getAllListComicByFilter(
            @Query("category") listCategoryId: List<Long>,
            @Query("status") status: String,
            @Query("type") type: String,
            @Query("items") limit: Int,
            @Query("page") offset: Int
    ) : LiveData<ApiResponse<List<ComicModel>>>

    @POST(ServerPath.GET_LIST_IMAGE_TO_DOWNLOAD)
    fun getListImageToDownload(
            @Path("comicId") comicId: Long,
            @Body dataGetListImageToDownloadParam: DataGetListImageToDownloadParam
    ) : LiveData<ApiResponse<List<ChapterModel>>>
}
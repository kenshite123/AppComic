package com.ggg.home.utils

class ServerPath {
    companion object {
        var baseUri = "http://144.76.71.134:8080/"
        const val LOGIN = "api/auth/login"
        const val REGISTER = "api/auth/register"
        const val BANNERS = "api/homepage/banners"
        const val LATEST_UPDATE = "api/homepage/latest-update"
        const val LIST_CATEGORIES = "api/comic/category-filters"
        const val LIST_COMIC = "api/comic/list"
        const val LIST_CHAPTERS = "api/comic/{comicId}/chapters"
        const val LIST_COMMENT_BY_COMIC = "api/comments/{comicId}"
        const val LIST_COMIC_RANKING = "api/homepage/top-ranking"
        const val LIST_COMIC_FAVORITE = "api/homepage/favorite-comics"
        const val WRITE_COMMENT = "api/comments/create"
        const val CHANGE_PASSWORD = "api/user/{id}/change-pwd"
        const val COMIC_INFO = "api/comic/{comicId}"
        const val FAVORITE_COMIC = "api/comic/{comicId}/favorite"
        const val UNFAVORITE_COMIC = "api/comic/{comicId}/unfavorite"
        const val LIST_COMIC_FOLLOW = "api/comic/watchlist"
        const val LOG_OUT = "api/auth/logout"
        const val GET_LIST_MY_COMMENT = "api/comments/mine"
        const val DELETE_COMMENT = "api/comments/{commentId}/remove"
        const val GET_COMMENT_DETAIL = "api/comments/{commentId}/detail"
        const val GET_LIST_COMMENT_OF_CHAP = "api/comments/{comicId}/chapter/{chapterId}"
        const val GET_LIST_IMAGE_OF_CHAP = "api/comic/{comicId}/chapters/{chapterId}"
    }
}

package com.ggg.home.utils

class ServerPath {
    companion object {
        var baseUri = "http://34.221.223.172:8080/heaven-manga/"
        const val LOGIN = "api/auth/login"
        const val REGISTER = "api/auth/register"
        const val BANNERS = "api/homepage/banners"
        const val LATEST_UPDATE = "api/homepage/latest-update"
        const val LIST_CATEGORIES = "api/comic/category-filters"
        const val LIST_COMIC = "api/comic/list"
        const val LIST_CHAPTERS = "api/comic/{comicId}/chapters"
        const val LIST_COMMENT_BY_COMIC = "api/comments/{comicId}"
        const val CHANGE_PASSWORD = "api/user/{id}/change-pwd"
        const val LOG_OUT = "api/auth/logout"
        const val LIST_COMIC_RANKING = "api/homepage/top-ranking"
        const val LIST_COMIC_FAVORITE = "api/homepage/favorite-comics"
        const val WRITE_COMMENT = "api/comments/create"
    }
}

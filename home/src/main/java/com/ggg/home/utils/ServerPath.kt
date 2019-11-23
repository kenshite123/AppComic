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
    }
}

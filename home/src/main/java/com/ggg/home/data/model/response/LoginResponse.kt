package com.ggg.home.data.model.response


import com.ggg.home.data.model.UserModel
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginResponse : Serializable {
        @SerializedName("accessToken")
        @Expose
        var accessToken: String = ""

        @SerializedName("tokenType")
        @Expose
        var tokenType: String = ""

        @SerializedName("user")
        @Expose
        var user: UserModel? = null

        fun convertToGson(): String {
                val gson = Gson()
                val json = gson.toJson(this)
                return json ?: ""
        }
}
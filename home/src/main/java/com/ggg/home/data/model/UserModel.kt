package com.ggg.home.data.model


import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserModel : Serializable {
        @SerializedName("deviceToken")
        @Expose
        var deviceToken: String = ""

        @SerializedName("email")
        @Expose
        var email: String = ""

        @SerializedName("fullName")
        @Expose
        var fullName: String = ""

        @SerializedName("group")
        @Expose
        var group: Int = 0

        @SerializedName("id")
        @Expose
        var id: Long = 0

        @SerializedName("imageUrl")
        @Expose
        var imageUrl: String = ""

        @SerializedName("mangaFollows")
        @Expose
        var mangaFollows: List<String> = listOf()

        @SerializedName("username")
        @Expose
        var username: String = ""

        fun convertToGson(): String {
                val gson = Gson()
                val json = gson.toJson(this)
                return json ?: ""
        }
}
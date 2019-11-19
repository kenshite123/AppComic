package com.ggg.home.data.model


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
        var id: Int = 0

        @SerializedName("imageUrl")
        @Expose
        var imageUrl: String = ""

        @SerializedName("mangaFollow")
        @Expose
        var mangaFollow: String = ""

        @SerializedName("username")
        @Expose
        var username: String = ""
}
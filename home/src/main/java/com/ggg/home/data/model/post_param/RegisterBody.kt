package com.ggg.home.data.model.post_param


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RegisterBody : Serializable {
        @SerializedName("email")
        @Expose
        var email: String = ""

        @SerializedName("fullName")
        @Expose
        var fullName: String = ""

        @SerializedName("password")
        @Expose
        var password: String = ""

        @SerializedName("userName")
        @Expose
        var userName: String = ""
}

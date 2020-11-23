package com.ggg.home.data.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RegisterResponse : Serializable {
        @SerializedName("message")
        @Expose
        var message: String = ""
}
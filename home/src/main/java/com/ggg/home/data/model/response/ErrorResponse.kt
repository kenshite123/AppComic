package com.ggg.home.data.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ErrorResponse : Serializable {
        @SerializedName("field")
        @Expose
        var `field`: String = ""

        @SerializedName("message")
        @Expose
        var message: String = ""
}
package com.ggg.home.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NoneResponse : Serializable {
    @SerializedName("mess")
    @Expose
    var mess: String = ""
}
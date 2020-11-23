package com.ggg.home.data.model.response

import com.ggg.home.data.model.UserModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChangePassWordResponse: Serializable {

    @SerializedName("accessToken")
    @Expose
    var accessToken: String = ""

    @SerializedName("tokenType")
    @Expose
    var tokenType: String = ""

    @SerializedName("user")
    @Expose
    var user: UserModel? = null
}
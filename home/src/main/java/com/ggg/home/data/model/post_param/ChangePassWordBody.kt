package com.ggg.home.data.model.post_param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChangePassWordBody: Serializable {
    @SerializedName("oldPassword")
    @Expose
    var oldPassword: String = ""

    @SerializedName("newPassword")
    @Expose
    var newPassword: String = ""

    @SerializedName("confirmPassword")
    @Expose
    var confirmPassword: String = ""

}
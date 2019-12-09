package com.ggg.home.data.model.response


import com.ggg.home.data.model.UserCommentModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CommentResponse : Serializable {
        @SerializedName("content")
        @Expose
        var content: String = ""

        @SerializedName("createdAt")
        @Expose
        var createdAt: String = ""

        @SerializedName("id")
        @Expose
        var commentId: Long = 0

        @SerializedName("type")
        @Expose
        var type: String = ""

        @SerializedName("user")
        @Expose
        var user: UserCommentModel = UserCommentModel()
}
package com.ggg.home.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CommentModel : Serializable {
        @SerializedName("comic")
        @Expose
        var comicModel: ComicModel = ComicModel()

        @SerializedName("type")
        @Expose
        var type: String = ""

        @SerializedName("content")
        @Expose
        var content: String = ""

        @SerializedName("chapter")
        @Expose
        var chapter: ChapterModel = ChapterModel()

        @SerializedName("createdAt")
        @Expose
        var createdAt: String = ""

        @SerializedName("id")
        @Expose
        var commentId: Long = 0

        @SerializedName("replies")
        @Expose
        var replies: List<CommentModel> = listOf()

        @SerializedName("user")
        @Expose
        var userComment: UserCommentModel = UserCommentModel()
}
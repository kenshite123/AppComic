package com.ggg.home.data.model


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(indices = [Index("commentId")], primaryKeys = ["commentId"])
class CommentModel : Serializable {
        @SerializedName("comicId")
        @Expose
        var comicId: Long = 0

        @SerializedName("content")
        @Expose
        var content: String = ""

        @SerializedName("createdAt")
        @Expose
        var createdAt: String = ""

        @SerializedName("id")
        @Expose
        var commentId: Long = 0

        @SerializedName("replies")
        @Expose
        @Ignore
        var replies: List<CommentModel> = listOf()

        @SerializedName("user")
        @Expose
        @Embedded
        var userComment: UserCommentModel = UserCommentModel()

        var commentParentId: Long = 0
}
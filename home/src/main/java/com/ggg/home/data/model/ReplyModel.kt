package com.ggg.home.data.model


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(indices = [Index("id")], primaryKeys = ["id"])
class ReplyModel : Serializable {
        @SerializedName("chapter")
        @Expose
        var chapter: String = ""

        @SerializedName("comicName")
        @Expose
        var comicName: String = ""

        @SerializedName("content")
        @Expose
        var content: String = ""

        @SerializedName("createdAt")
        @Expose
        var createdAt: String = ""

        @SerializedName("id")
        @Expose
        var id: Int = 0

        @SerializedName("user")
        @Expose
        @Ignore
        var userComment: UserCommentModel = UserCommentModel()
}
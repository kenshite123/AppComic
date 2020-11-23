package com.ggg.home.data.model.post_param


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WriteCommentBody : Serializable {
        @SerializedName("chapterId")
        @Expose
        var chapterId: Long = 0

        @SerializedName("comicId")
        @Expose
        var comicId: Long = 0

        @SerializedName("content")
        @Expose
        var content: String = ""

        @SerializedName("parentId")
        @Expose
        var parentId: Long = 0

        @SerializedName("topicType")
        @Expose
        var topicType: String = ""
}
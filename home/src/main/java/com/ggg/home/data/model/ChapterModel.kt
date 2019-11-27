package com.ggg.home.data.model


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.ggg.home.utils.Constant
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(indices = [Index("chapterId")], primaryKeys = ["chapterId"])
class ChapterModel : Serializable {
        @SerializedName("chapterId")
        @Expose
        var chapterId: Long = 0

        @SerializedName("chapterName")
        @Expose
        var chapterName: String = ""

        @SerializedName("dateUpdate")
        @Expose
        var dateUpdate: String = ""

        @SerializedName("imageUrls")
        @Expose
        @Ignore
        var imageUrls: List<String> = listOf()

        @SerializedName("status")
        @Expose
        var status: Int = 0

        var comicId: Long = 0
        var listImageUrlString: String = ""
        var lastModified: Long = System.currentTimeMillis()
}
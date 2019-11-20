package com.ggg.home.data.model


import android.text.TextUtils
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(indices = [Index("id")], primaryKeys = ["id"])
class ComicModel : Serializable {
        @SerializedName("id")
        @Expose
        var id: Int = 0

        @SerializedName("author")
        @Expose
        var author: String = ""

        @SerializedName("bigImageUrl")
        @Expose
        var bigImageUrl: String = ""

        @SerializedName("categories")
        @Expose
        @Ignore
        var categories: List<Int> = listOf()

        @SerializedName("content")
        @Expose
        var content: String = ""

        @SerializedName("imageUrl")
        @Expose
        var imageUrl: String = ""

        @SerializedName("rate")
        @Expose
        var rate: String = ""

        @SerializedName("title")
        @Expose
        var title: String = ""

        @SerializedName("titleSeo")
        @Expose
        var titleSeo: String = ""

        @SerializedName("viewed")
        @Expose
        var viewed: Int = 0

        @SerializedName("vote")
        @Expose
        var vote: Int = 0

        var categoriesString: String = ""

        constructor() {
                categoriesString = TextUtils.join(",", categories)
        }
}
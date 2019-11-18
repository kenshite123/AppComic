package com.ggg.home.data.model

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(indices = [Index("id")], primaryKeys = ["id"])
class ComicModel : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("thumb")
    @Expose
    var thumb: String = ""

    @SerializedName("viewed")
    @Expose
    var viewed: Int = 0

    @SerializedName("big_thumb")
    @Expose
    var bigThumb: String = ""

    @SerializedName("slider")
    @Expose
    var slider: Int = 0

    @SerializedName("decu")
    @Expose
    var decu: Int = 0

    @SerializedName("author")
    @Expose
    var author: String = ""

    @SerializedName("category")
    @Expose
    var category: String = ""

    @SerializedName("status")
    @Expose
    var status: Int = 0

    @SerializedName("fansub")
    @Expose
    var fansub: String = ""

    @SerializedName("content")
    @Expose
    var content: String = ""

    @SerializedName("viewed_day")
    @Expose
    var viewedDay: Int = 0

    @SerializedName("viewed_week")
    @Expose
    var viewedWeek: Int = 0

    @SerializedName("viewed_month")
    @Expose
    var viewedMonth: Int = 0

    @SerializedName("timeupdate")
    @Expose
    var timeUpdate: Long = 0

    @SerializedName("rate")
    @Expose
    var rate: Float = 0F

    @SerializedName("vote")
    @Expose
    var vote: Int = 0
}
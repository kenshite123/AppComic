package com.ggg.home.data.model.post_param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DataSendReportParam : Serializable {
    @SerializedName("comicId")
    @Expose
    var comicId: Long = 0L

    @SerializedName("chapterId")
    @Expose
    var chapterId: Long = 0L

    @SerializedName("content")
    @Expose
    var content: String = ""
}
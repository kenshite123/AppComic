package com.ggg.home.data.model.post_param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DataGetListImageToDownloadParam : Serializable {
    @SerializedName("chapterIds")
    @Expose
    var chapterIds: List<Long> = listOf()
}
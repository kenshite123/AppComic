package com.ggg.home.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

class ComicDownloadedModel : Serializable {
    @Embedded
    var comicModel: ComicModel? = null

    @Relation(parentColumn = "id", entityColumn = "comicId"/*, entity = CategoryOfComicModel::class*/)
    var listChapterModel: List<ChapterModel>? = null
}
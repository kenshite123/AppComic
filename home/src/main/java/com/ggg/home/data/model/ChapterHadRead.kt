package com.ggg.home.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

class ChapterHadRead : Serializable {
    @Embedded
    var chapterModel: ChapterModel? = null

    @Relation(parentColumn = "chapterId", entityColumn = "chapterId", entity = CCHadReadModel::class)
    var ccHadReadModel: List<CCHadReadModel>? = null
}
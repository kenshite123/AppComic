package com.ggg.home.data.model

import androidx.room.Embedded
import androidx.room.Relation

class HistoryModel {
    @Embedded
    var comicModel: ComicModel? = null

    @Relation(parentColumn = "id", entityColumn = "comicId", entity = CCHadReadModel::class)
    var ccHadReadModels: List<CCHadReadModel>? = null
}
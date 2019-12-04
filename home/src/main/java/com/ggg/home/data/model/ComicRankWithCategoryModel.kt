package com.ggg.home.data.model

import androidx.room.Embedded
import androidx.room.Relation

class ComicRankWithCategoryModel {
    @Embedded
    var comicRankModel: ComicRankModel? = null

    @Relation(parentColumn = "id", entityColumn = "comicId", entity = CategoryOfComicModel::class)
    var categories: List<CategoryOfComicModel>? = null
}
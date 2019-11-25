package com.ggg.home.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable


class ComicWithCategoryModel : Serializable {
    @Embedded
    var comicModel: ComicModel? = null

    @Relation(parentColumn = "id", entityColumn = "comicId", entity = CategoryOfComicModel::class)
    var categories: List<CategoryOfComicModel>? = null
}
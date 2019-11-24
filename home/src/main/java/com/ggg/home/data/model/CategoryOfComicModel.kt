package com.ggg.home.data.model

import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(indices = [Index("comicId", "categoryId")], primaryKeys = ["comicId", "categoryId"])
class CategoryOfComicModel : Serializable {
    var comicId: Long = 0
    var categoryId: Long = 0
    var categoryName: String = ""
}
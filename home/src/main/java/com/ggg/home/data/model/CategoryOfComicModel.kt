package com.ggg.home.data.model

import androidx.room.Entity
import androidx.room.Index

@Entity(indices = [Index("comicId", "categoryId")], primaryKeys = ["comicId", "categoryId"])
class CategoryOfComicModel {
    var comicId: Long = 0
    var categoryId: Long = 0
    var categoryName: String = ""
}
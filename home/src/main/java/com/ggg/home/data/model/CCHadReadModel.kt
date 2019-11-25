package com.ggg.home.data.model

import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

// comic and chapter
@Entity(indices = [Index("comicId", "chapterId")], primaryKeys = ["comicId", "chapterId"])
class CCHadReadModel : Serializable {
    var comicId: Long = 0
    var chapterId: Long = 0
    var positionOfPage: Int = 0
}
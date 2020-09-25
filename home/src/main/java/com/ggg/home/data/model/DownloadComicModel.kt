package com.ggg.home.data.model

import android.database.Cursor
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.ggg.common.utils.CursorUtil
import com.ggg.common.utils.Utils
import com.ggg.home.utils.Constant
import java.io.Serializable

@Entity(indices = [Index("id")], primaryKeys = ["id"])
class DownloadComicModel : Serializable {
    constructor()
    constructor(srcImg: String, comicId: Long, chapterId: Long, hadDownloaded: Int) {
        this.id = Utils.md5(srcImg)
        this.srcImg = srcImg
        this.comicId = comicId
        this.chapterId = chapterId
        this.hadDownloaded = hadDownloaded
    }

    var id: String = ""
    var srcImg: String = ""
    var comicId: Long = 0
    var chapterId: Long = 0
    var hadDownloaded: Int = Constant.IS_NOT_DOWNLOAD
    @Ignore
    var totalDownloaded = 0
    @Ignore
    var totalNotDownloaded = 0
}
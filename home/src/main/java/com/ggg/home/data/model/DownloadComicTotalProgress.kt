package com.ggg.home.data.model

import java.io.Serializable

class DownloadComicTotalProgress : Serializable {
    var comicId: Long = 0
    var totalDownloaded = 0
    var totalNotDownloaded = 0
}
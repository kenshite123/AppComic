package com.ggg.home.data.model


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(indices = [Index("id")], primaryKeys = ["id"])
class CategoryModel : Serializable {
        @SerializedName("id")
        @Expose
        var id: Long = 0

        @SerializedName("name")
        @Expose
        var name: String = ""

        @SerializedName("status")
        @Expose
        var status: Int = 0

        var lastModified: Long = System.currentTimeMillis()

        @Ignore
        var isChoose: Boolean = false
}
package com.ggg.home.data.model


import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(indices = [Index("id")], primaryKeys = ["id"])
class UserCommentModel : Serializable {
        @SerializedName("id")
        @Expose
        var id: Int = 0

        @SerializedName("imageUrl")
        @Expose
        var imageUrl: String = ""

        @SerializedName("nickname")
        @Expose
        var nickname: String = ""
}
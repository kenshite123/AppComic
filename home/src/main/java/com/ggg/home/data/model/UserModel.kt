package com.ggg.home.data.model

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(indices = arrayOf(Index("userId")), primaryKeys = arrayOf("userId"))
class UserModel {

    @SerializedName("userId")
    @Expose
    var userId: Long? = null
}
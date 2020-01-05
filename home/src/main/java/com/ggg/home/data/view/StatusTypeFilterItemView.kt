package com.ggg.home.data.view

import java.io.Serializable

class StatusTypeFilterItemView : Serializable {
    var statusType: String = ""
    var isSelected = false
    var isType = false

    constructor()

    constructor(statusType: String, isSelected: Boolean, isType: Boolean) {
        this.statusType = statusType
        this.isType = isType
        this.isSelected = isSelected
    }
}
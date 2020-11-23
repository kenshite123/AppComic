package com.ggg.home.data.view

import com.ggg.home.data.model.CategoryModel
import java.io.Serializable

class CategoryFilterItemView : Serializable {
    var categoryModel: CategoryModel = CategoryModel()
    var isSelected = false
    var isAll = false

    constructor()

    constructor(categoryModel: CategoryModel, isSelected: Boolean, isAll: Boolean) {
        this.categoryModel = categoryModel
        this.isAll = isAll
        this.isSelected = isSelected
    }
}
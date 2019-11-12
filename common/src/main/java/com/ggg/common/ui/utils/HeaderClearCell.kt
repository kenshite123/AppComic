package com.ggg.common.ui.utils

import android.content.Context
import android.util.AttributeSet
import java.util.HashMap

class HeaderClearCell : BaseInfoCell {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init(context: Context, attrs: AttributeSet) {
        super.init(context, attrs)
    }

    override fun setSectionDisplay(displayInfo: HashMap<String, Any>, expanded: Boolean) {
        super.setSectionDisplay(displayInfo, expanded)
    }
}
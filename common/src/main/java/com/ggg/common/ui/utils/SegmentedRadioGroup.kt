package com.ggg.common.ui.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup
import com.ggg.common.R

class SegmentedRadioGroup : RadioGroup {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onFinishInflate() {
        super.onFinishInflate()
        changeButtonsImages()
    }

    private fun changeButtonsImages() {
        val count = super.getChildCount()

        if (count > 1) {

            super.getChildAt(0).setBackgroundResource(R.drawable.segment_radio_left)
            for (i in 1 until count - 1) {
                super.getChildAt(i).setBackgroundResource(R.drawable.segment_radio_middle)
            }
            super.getChildAt(count - 1).setBackgroundResource(R.drawable.segment_radio_right)
        } else if (count == 1) {
            super.getChildAt(0).setBackgroundResource(R.drawable.segment_button)
        }
    }
}
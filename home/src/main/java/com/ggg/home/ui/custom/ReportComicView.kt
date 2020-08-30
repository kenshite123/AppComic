package com.ggg.home.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.layout_report_comic_view.view.*
import java.lang.ref.WeakReference

class ReportComicView : ConstraintLayout {
    lateinit var mContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var content = ""

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = WeakReference(context)
        View.inflate(context, R.layout.layout_report_comic_view, this)
        initViews()
        addEvents()
    }

    private fun initViews() {

    }

    private fun addEvents() {
        ctlInfo.setOnClickListener {  }
        v1.setOnClickListener {
            listener.onEvent(Constant.ACTION_HIDE_REPORT_COMIC_VIEW, null, null)
        }
        tvSubmit.setOnClickListener {
            listener.onEvent(Constant.ACTION_SEND_REPORT, null, content)
        }
        rd1.setOnCheckedChangeListener { _, isChecked ->
            checkEnableButtonSubmit()
            if (isChecked) {
                content = rd1.text.toString()
            }
        }
        rd2.setOnCheckedChangeListener { _, isChecked ->
            checkEnableButtonSubmit()
            if (isChecked) {
                content = rd2.text.toString()
            }
        }
        rd3.setOnCheckedChangeListener { _, isChecked ->
            checkEnableButtonSubmit()
            if (isChecked) {
                content = rd3.text.toString()
            }
        }
        rd4.setOnCheckedChangeListener { _, isChecked ->
            checkEnableButtonSubmit()
            if (isChecked) {
                content = rd4.text.toString()
            }
        }
        rd5.setOnCheckedChangeListener { _, isChecked ->
            checkEnableButtonSubmit()
            if (isChecked) {
                content = rd5.text.toString()
            }
        }
        rd6.setOnCheckedChangeListener { _, isChecked ->
            checkEnableButtonSubmit()
            if (isChecked) {
                content = rd6.text.toString()
            }
        }
    }

    private fun checkEnableButtonSubmit() {
        if (rd1.isChecked || rd2.isChecked || rd3.isChecked || rd4.isChecked || rd5.isChecked || rd6.isChecked ) {
            tvSubmit.setBackgroundResource(R.drawable.bg_btn_submit_report_enable)
            tvSubmit.isClickable = true
        } else {
            tvSubmit.setBackgroundResource(R.drawable.bg_btn_submit_report_disable)
            tvSubmit.isClickable = false
            content = ""
        }
    }

    fun resetView() {
        rd1.isChecked = false
        rd2.isChecked = false
        rd3.isChecked = false
        rd4.isChecked = false
        rd5.isChecked = false
        rd6.isChecked = false
        content = ""
        checkEnableButtonSubmit()
    }
}
package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.StringUtil
import com.ggg.home.R
import java.lang.ref.WeakReference

class PagerLibraryAdapter : PagerAdapter {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var tvTest: TextView
    var listTitle: ArrayList<String> = arrayListOf(StringUtil.getString(R.string.TEXT_HISTORY),
            StringUtil.getString(R.string.TEXT_FOLLOW), StringUtil.getString(R.string.TEXT_DOWNLOAD))

    constructor(context: Context, listener: OnEventControlListener) {
        this.weakContext = WeakReference(context)
        this.listener = listener
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_library, container, false)
        tvTest = view.findViewById(R.id.tvTest)

        tvTest.text = "Tab ${listTitle[position]}"

        container.addView(view)
        return  view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}
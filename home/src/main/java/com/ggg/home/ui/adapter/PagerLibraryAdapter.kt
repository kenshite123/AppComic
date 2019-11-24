package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.StringUtil
import com.ggg.home.R
import java.lang.ref.WeakReference

class PagerLibraryAdapter : PagerAdapter {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
//    lateinit var rvListComic: RecyclerView

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
        return listTitle.count()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_library, container, false)
//        rvListComic = view.findViewById(R.id.rvListComic)
        val ivComic = view.findViewById<ImageView>(R.id.ivComic)
        Glide.with(weakContext.get())
                .load("http://ww5.heavenmanga.org/content/upload/images/images/Solo-Leveling.jpg")
//                .load("http://ww5.heavenmanga.org/include/lib/security.php?1574219282")
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivComic)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}
package com.ggg.home.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.ggg.common.ui.BaseActivity
import com.ggg.common.utils.StringUtil
import com.ggg.home.R
import java.util.*

class PagerAdapter : FragmentPagerAdapter {
    private val fragments: MutableList<Fragment>
    var listTitle: ArrayList<String> = arrayListOf(StringUtil.getString(R.string.TEXT_LATEST_UPDATE), StringUtil.getString(R.string.TEXT_ALL))

    constructor(activity: BaseActivity, fragments: MutableList<Fragment>) : super(activity.supportFragmentManager) {
        this.fragments = fragments
    }

    constructor(activity: BaseActivity) : super(activity.supportFragmentManager) {
        fragments = ArrayList()
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }
}
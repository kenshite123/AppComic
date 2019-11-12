package com.ggg.common.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * Created by TuanNguyen on 12/12/17.
 */
class AutoClearedValue<T> {
    private var value: T?

    constructor(fragment: Fragment, value: T) {
        val fragmentManager = fragment.fragmentManager
        if (fragmentManager != null) {
            fragmentManager.registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                            if(f==fragment) {
                                this@AutoClearedValue.value = null
                                fragmentManager.unregisterFragmentLifecycleCallbacks(this)
                            }
                        }
                    }, false)
        }
        this.value = value
    }

    fun get(): T? {
        return value
    }
}
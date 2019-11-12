package com.ggg.app.ui.init

import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.ggg.app.R
import com.ggg.home.ui.login.LoginActivity
import com.ggg.home.ui.main.MainActivity
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by TuanNguyen on 12/12/17.
 */

class NavigationController @Inject constructor(activity: InitialActivity) {

    private val containerId = R.id.container
    private val fragmentManager: FragmentManager = activity.supportFragmentManager
    private var weakActivity = WeakReference(activity)
    fun showSplash(){
        val fragment: SplashFragment = SplashFragment()
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commitAllowingStateLoss()
    }

    fun showHomeModule(){
        if (weakActivity.get() != null){
            var activity = weakActivity.get()
            val intent = Intent(activity, MainActivity::class.java)
            activity!!.startActivity(intent)
        }
    }

    fun showLoginModule(){
        if (weakActivity.get() != null){
            var activity = weakActivity.get()
            val intent = Intent(activity, LoginActivity::class.java)
            activity!!.startActivity(intent)
        }
    }

}
package com.ggg.home.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.ggg.common.GGGAppInterface
import com.ggg.common.ui.BaseActivity
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.ui.category.CategoryFragment
import com.ggg.home.ui.category_and_latest_update.CategoryAndLatestUpdateFragment
import com.ggg.home.ui.comic_detail.ComicDetailFragment
import com.ggg.home.ui.home.HomeFragment
import com.ggg.home.ui.home.HomeViewModel
import com.ggg.home.ui.library.LibraryFragment
import com.ggg.home.ui.search.SearchFragment
import com.ggg.home.ui.user.UserFragment
import com.ggg.home.utils.Constant
import com.ncapdevi.fragnav.FragNavController
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector, FragNavController.RootFragmentListener {

    private val registry = LifecycleRegistry(this)
    override fun getLifecycle(): LifecycleRegistry {
        return registry
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatch
    }

    override val numberOfRootFragments: Int = 1

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            0 -> return HomeFragment.create()
        }
        throw IllegalStateException("Need to send an index that we know")
    }

    @Inject
    lateinit var navigationController: NavigationController
    private lateinit var viewModel: MainViewModel
    var isFirstLoad = true
    var fragNavController: FragNavController = FragNavController(this.supportFragmentManager, R.id.container)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        val isShowComicDetail = intent.getBooleanExtra("isShowComicDetail", false)
        val fragments: List<Fragment>

        if (isShowComicDetail) {
            GGGAppInterface.gggApp.isFromNotification = true
            val comicId = intent.getStringExtra("comicId").toString()
            fragments = listOf(HomeFragment.create(comicId))
        } else {
            fragments = listOf(HomeFragment.create())
        }

        fragNavController.rootFragments = fragments
        fragNavController.initialize(0, savedInstanceState)
        ivLoading.setImageResource(R.drawable.loading)
        rltLoading.bringToFront()
        initEvents()
        AndroidNetworking.initialize(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState!!)
    }

    override fun onBackPressed() {
        when {
            fragNavController.isRootFragment.not() -> {
                fragNavController.popFragment()
                hideSoftKeyboard()

                fragNavController.currentFrag?.let {
                    when (it::class.java.simpleName) {
                        HomeFragment.TAG -> {
                            bottomNavView.selectedItemId = R.id.navHome
                        }

                        CategoryAndLatestUpdateFragment.TAG -> {
                            bottomNavView.selectedItemId = R.id.navCategory
                        }

                        SearchFragment.TAG -> {
                            bottomNavView.selectedItemId = R.id.navSearch
                        }

                        LibraryFragment.TAG -> {
                            bottomNavView.selectedItemId = R.id.navLib
                        }

                        UserFragment.TAG -> {
                            bottomNavView.selectedItemId = R.id.navUser
                        }

                        else -> {

                        }
                    }
                }
            }
            else -> {
//                normalBack()
                navigationController.showLaunchScreen()
            }
        }
    }

    fun normalBack() {
        super.onBackPressed()
    }

    fun hideSoftKeyboard() {
        if (this.currentFocus != null) {
            val inputMethodManager = this.getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                    this.currentFocus!!.windowToken, 0)
        }
    }

    override fun showBottomNavView() {
        bottomNavView.visibility = View.VISIBLE
    }

    override fun hideBottomNavView() {
        bottomNavView.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initEvents() {
        rltLoading.setOnClickListener {

        }

        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navHome -> {
                    fragNavController.currentFrag?.let {
                        if (it::class.java.simpleName != HomeFragment.TAG) {
                            navigationController.showHome()
                        }
                    }
                    true
                }

                R.id.navCategory -> {
                    fragNavController.currentFrag?.let {
                        if (it::class.java.simpleName != CategoryAndLatestUpdateFragment.TAG) {
                            navigationController.showCategoryAndLatestUpdate()
                        }
                    }
                    true
                }

                R.id.navSearch -> {
                    fragNavController.currentFrag?.let {
                        if (it::class.java.simpleName != SearchFragment.TAG) {
                            navigationController.showSearch()
                        }
                    }
                    true
                }

                R.id.navLib -> {
                    fragNavController.currentFrag?.let {
                        if (it::class.java.simpleName != LibraryFragment.TAG) {
                            navigationController.showLibrary()
                        }
                    }
                    true
                }

                R.id.navUser -> {
                    fragNavController.currentFrag?.let {
                        if (it::class.java.simpleName != UserFragment.TAG) {
                            navigationController.showUser()
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }

    override fun showScreenById(resourceId: Int) {
        bottomNavView.selectedItemId = resourceId
    }

    override fun showLoading() {
        rltLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rltLoading.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }

    private fun initObserver() {
    }

    fun processDownloadImage(listImageDownload: MutableList<HashMap<String, Any>>) {
        doAsync {
            listImageDownload.forEach {
                val comicId = it["comicId"] as Long
                val chapterId = it["chapterId"] as Long
                val imageUrl = it["imageUrl"] as String
                val imgSplit = imageUrl.split("/")
                val fileName = imgSplit[imgSplit.count() - 1]

                val downloadPath = "${filesDir.absolutePath}/DownloadComic/${comicId}/${chapterId}/"
                val downloadFolder = File(downloadPath)
                downloadFolder.mkdirs()
//                val file = File(downloadPath, fileName)

                AndroidNetworking.download(imageUrl, downloadPath, fileName)
                        .build()
                        .startDownload(object : DownloadListener {
                            override fun onDownloadComplete() {
                                Log.d("MainActivity", "Download Success ${it.toString()}")
                                processDownloadImageSuccess(imageUrl, chapterId, comicId)
                            }

                            override fun onError(anError: ANError?) {
                                Log.d("MainActivity", "Download fail: ${anError?.errorBody} - ${anError?.errorDetail}")
                            }
                        })
//                Glide.with(GGGAppInterface.gggApp.ctx)
//                        .load(imageUrl)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .signature(ObjectKey(Constant.SIGNATURE_IMAGE_CACHE))
//                        .downloadOnly(object : SimpleTarget<File?>() {
//                            override fun onLoadFailed(errorDrawable: Drawable?) {
//                                super.onLoadFailed(errorDrawable)
//                                Log.e("MainActivity", it.toString())
//                                processDownloadImageSuccess(imageUrl, chapterId, comicId)
//                            }
//
//                            override fun onResourceReady(resource: File, transition: Transition<in File?>?) {
//                                Log.d("MainActivity", it.toString())
//                                processDownloadImageSuccess(imageUrl, chapterId, comicId)
//                            }
//                        })
            }
        }
    }

    fun processDownloadImageSuccess(imageUrl: String, chapterId: Long, comicId: Long) {
        doAsync {
            viewModel.updateDownloadedComic(imageUrl)
            GGGAppInterface.gggApp.updateDownloadImageComicSuccess(chapterId)
            val result = GGGAppInterface.gggApp.checkDownloadDone(chapterId)
            if (result) {
                GGGAppInterface.gggApp.removeComicDownloadFromHashMap(chapterId)
                viewModel.updateChapDownloaded(chapterId = chapterId)
                val hm = hashMapOf(
                        "comicId" to comicId,
                        "chapterId" to chapterId
                )
                GGGAppInterface.gggApp.bus().sendDownloadImageDone(hm = hm)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.updateListDownloadingToNotDownload()
    }
}

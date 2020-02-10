package com.ggg.app.ui.init

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ggg.app.R
import com.ggg.common.GGGAppInterface
import com.ggg.common.di.Injectable
import com.ggg.common.utils.combineLatest
import com.ggg.common.vo.Status
import org.jetbrains.anko.bundleOf
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SplashFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : Fragment(),Injectable{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel:InitViewModel
    var isFirstLoad = true
    var isShowComicDetail: Boolean = false
    var comicId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(InitViewModel::class.java)

        if (arguments == null) {
            loadData()
        } else {
            if (arguments!!["isShowComicDetail"] != null) {
                isShowComicDetail = arguments!!["isShowComicDetail"] as Boolean
                comicId = arguments!!["comicId"].toString()

                viewModel.getListChapters(comicId.toLong())
            }
        }
    }

    private fun loadData() {
        viewModel.getConfig()
        viewModel.getBanners()
    }

    private fun initObserve() {
        viewModel.getBannersResult.observe(this, Observer {
            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                if (isShowComicDetail) {
                    (activity as InitialActivity).navigationController.showHomeModule(isShowComicDetail, comicId)
                } else {
                    (activity as InitialActivity).navigationController.showHomeModule()
                }
                (activity as InitialActivity).finish()
            }
        })

        viewModel.getListChaptersResult.observe(this, Observer {
            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                (activity as InitialActivity).navigationController.showHomeModule(isShowComicDetail, comicId)
                (activity as InitialActivity).finish()
            }
        })

        viewModel.getConfigModelResult.observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    if (!it.siteDeploy.isEmpty() &&
                            (it.siteDeploy.toLowerCase().contains("true") || it.siteDeploy.toLowerCase().contains("false"))) {
                        GGGAppInterface.gggApp.siteDeploy = it.siteDeploy.toLowerCase().contains("true")
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserve()
            isFirstLoad = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.getBannersResult.removeObservers(this)
        viewModel.getConfigModelResult.removeObservers(this)
//        viewModel.getListLatestUpdateResult.removeObservers(this)
//        viewModel.getBannersResult.combineLatest(viewModel.getListLatestUpdateResult).removeObservers(this)
    }

    companion object {
        val TAG = "SplashFragment"
        @JvmStatic
        fun create() = SplashFragment()

        @JvmStatic
        fun create(isShowComicDetail: Boolean, comicId: String): SplashFragment {
            val fragment = SplashFragment()
            val bundle = bundleOf(
                    "isShowComicDetail" to isShowComicDetail,
                    "comicId" to comicId
            )
            fragment.arguments = bundle
            return fragment
        }
    }

}

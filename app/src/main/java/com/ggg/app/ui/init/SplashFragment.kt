package com.ggg.app.ui.init

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ggg.app.R
import com.ggg.common.di.Injectable
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(InitViewModel::class.java)

        /* Load data in splash screen
        viewModel.dataObs.observe(this, Observer {
            if (it!!.status == Status.LOADING){
                progressBar.visibility = View.VISIBLE
            }else if(it!!.status == Status.SUCCESS){
                Timber.d("Success")
                (activity as InitialActivity).navigationController.showHomeModule()
                activity.finish()
            }else if (it.status == Status.ERROR){
                Timber.d(it.message?:"")
                indefiniteSnackbar(splashRoot,"Cập nhật dữ liệu thất bại","Thử lại",{
                    viewModel.getData()
                })
                progressBar.visibility = View.GONE
            }
        })
        viewModel.getData()
        */

        (activity as InitialActivity).navigationController.showHomeModule()
        (activity as InitialActivity).finish()
    }

}// Required empty public constructor

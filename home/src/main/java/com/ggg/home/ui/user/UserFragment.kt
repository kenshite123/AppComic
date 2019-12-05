package com.ggg.home.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.PrefsUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_user.*
import timber.log.Timber

class UserFragment : HomeBaseFragment() {
    private lateinit var viewModel: UserViewModel
    var isFirstLoad = true
    var loginResponse: LoginResponse? = null

    companion object {
        val TAG = "UserFragment"
        @JvmStatic
        fun create() = UserFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)
        hideActionBar()
        showBottomNavView()

        this.loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse?

        initViews()
        initEvent()
    }

    private fun initViews() {
        loginResponse?.let {
            it.user?.let {
                tvUsername.text = it.fullName
                if (!it.imageUrl.isEmpty()) {
                    Glide.with(context)
                            .load(it.imageUrl)
                            .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(ivAvatar)
                } else {
                    ivAvatar.setImageResource(R.drawable.i_avatar)
                }
            }

            btnLogout.visibility = View.VISIBLE
            btnChangePass.visibility = View.VISIBLE
        }
    }

    override fun initObserver() {
        viewModel.logOutResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                Log.d("LogOut", "Success")
                showDialog(R.string.TEXT_LOG_OUT_SUCCESS)
                updateUI()
            } else if (it.status == Status.ERROR) {
                showDialog(it.message.toString())
            }

        })
    }

    private fun updateUI() {
        PrefsUtil.instance.clear()
        btnChangePass.visibility = View.GONE
        btnLogout.visibility = View.GONE
        tvNameUser.text = R.string.TEXT_LOGIN.toString()
        Glide.with(context)
                .load(R.drawable.i_avatar)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivAvatar)
    }

    override fun initEvent() {
        llLogin.setOnClickListener {
            if (loginResponse == null) {
                navigationController.showLogin()
            }
        }

        btnChangePass.setOnClickListener {
            navigationController.showChangePassWord()
        }

        btnLogout.setOnClickListener {
            var token: String = ""
            if (loginResponse != null) {
                token = loginResponse?.tokenType + loginResponse?.accessToken
            }
            val param = hashMapOf(
                    "token" to token
            )
            viewModel.logOut(param)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }
}
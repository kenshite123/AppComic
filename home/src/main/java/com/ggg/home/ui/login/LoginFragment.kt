package com.ggg.home.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ggg.common.utils.StringUtil
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.ui.main.HomeBaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber

class LoginFragment : HomeBaseFragment() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        showActionBar()
        hideBottomNavView()
        setTitleActionBar(StringUtil.getString(R.string.TEXT_LOGIN))

        initViews()
        initObserver()
        initEvent()
    }

    private fun initViews() {

    }

    override fun initObserver() {
        viewModel.loginResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                showDialog(R.string.TEXT_LOGIN_SUCCESS)
                navigationController.popToBackStack()
            } else if (it.status == Status.ERROR) {
                showDialog(it.message.toString())
            }
        })
    }

    override fun initEvent() {
        btnLogin.setOnClickListener {
            hideSoftKeyboard()
            if (checkValidData()) {
                val data = hashMapOf(
                        "username" to edUsername.text.toString(),
                        "password" to edPassword.text.toString(),
//                    "token" to "123456789"
                        "token" to "123"
                )

                viewModel.requestLogin(data)
            }
        }
    }

    private fun checkValidData(): Boolean {
        if (edUsername.text.toString().isNullOrEmpty()) {
            showMsg(R.string.TEXT_ERROR_INPUT_USERNAME_OR_PASSWORD_EMPTY)
            edUsername.requestFocus()
            return false
        }

        if (edPassword.text.toString().isNullOrEmpty()) {
            showMsg(R.string.TEXT_ERROR_INPUT_PASSWORD_EMPTY)
            edPassword.requestFocus()
            return false
        }

        return true
    }

    companion object {
        val TAG = "LoginFragment"
        @JvmStatic
        fun create() = LoginFragment()
    }
}
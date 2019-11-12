package com.ggg.home.ui.login.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ggg.home.R
import com.ggg.home.ui.login.LoginBaseFragment


class LoginFragment : LoginBaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    companion object {
        val TAG = "LoginFragment"
        @JvmStatic
        fun create() =
                LoginFragment()
    }
}

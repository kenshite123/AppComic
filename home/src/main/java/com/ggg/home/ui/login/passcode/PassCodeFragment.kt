package com.ggg.home.ui.login.passcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ggg.home.R
import com.ggg.home.ui.login.LoginBaseFragment

class PassCodeFragment : LoginBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass_code, container, false)
    }


    companion object {
        val TAG = "PassCodeFragment"
        @JvmStatic
        fun create() =
                PassCodeFragment()
    }
}

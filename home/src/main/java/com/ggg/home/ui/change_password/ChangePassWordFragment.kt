package com.ggg.home.ui.change_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.PrefsUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_change_password.*
import timber.log.Timber

class ChangePassWordFragment: HomeBaseFragment() {
    private lateinit var viewModel: ChangePassWordViewModel
    var loginResponse: LoginResponse? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangePassWordViewModel::class.java)

        val jsonLoginResponse = PrefsUtil.instance.getStringValue("LoginResponse", "")
        if (!jsonLoginResponse.isNullOrEmpty()) {
            this.loginResponse = Gson().fromJson<LoginResponse>(
                    jsonLoginResponse, object : TypeToken<LoginResponse>() {}.type)
        }

        initViews()
        initObserver()
        initEvent()
    }

    private fun initViews() {
        hideActionBar()
        hideBottomNavView()

    }

    override fun initObserver() {
        viewModel.changePassWordResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                showDialog(R.string.TEXT_CHANGE_PASSWORD_SUCCESS)
                navigationController.popToBackStack()
            } else if (it.status == Status.ERROR) {
                showDialog(it.message.toString())
            }
        })

    }

    override fun initEvent() {
        btnChangePass.setOnClickListener {
            if (checkValidChangePass()) {
                val id: String? = loginResponse?.user?.id.toString()
                var token: String = ""
                if (loginResponse != null) {
                    token = loginResponse?.tokenType + loginResponse?.accessToken
                }
                val param = hashMapOf(
                        "token" to token,
                        "id" to id!!,
                        "oldPassword" to edtOldPass.text.toString(),
                        "newPassword" to edtNewPass.text.toString(),
                        "confirmPassword" to edtConfirmNewPass.text.toString()
                )
                viewModel.changePassWord(param)
            }
        }
    }

    private fun checkValidChangePass(): Boolean {
        return when {
            !checkEmpty(edtOldPass) -> {
                showDialog(R.string.TEXT_FULLNAME_NOT_EMPTY)
                false
            }
            !checkEmpty(edtNewPass) -> {
                showDialog(R.string.TEXT_FULLNAME_NOT_EMPTY)
                false
            }
            !checkValidPassword(edtNewPass, edtConfirmNewPass)-> {
                showDialog(R.string.TEXT_NEWPASS_NOT_MATCH)
                false
            }
            !checkSizePassword(edtNewPass.text.toString()) -> {
                showDialog(R.string.TEXT_PASSWORD_MORE_6_CHAR)
                false
            }
            else -> true

        }
    }

    private fun checkEmpty(view: EditText): Boolean {
        if (view.text.toString().isBlank()) {
            return false
        }
        return true
    }

    private fun checkSizePassword(password: String?) = password?.length ?: 0 >= 6

    private fun checkValidPassword(edtNewPass: EditText, edtConfirmNewPass: EditText): Boolean {
        if (edtNewPass.text.toString() != edtConfirmNewPass.text.toString()) {
            return false
        }
        return true
    }

    companion object{
        val TAG = "ChangePassWordFragment"
        @JvmStatic
        fun create() = ChangePassWordFragment()
    }

}
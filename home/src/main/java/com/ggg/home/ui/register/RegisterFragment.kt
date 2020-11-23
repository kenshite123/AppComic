package com.ggg.home.ui.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.ui.main.HomeBaseFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.btnRegister
import test.jinesh.captchaimageviewlib.CaptchaImageView
import timber.log.Timber

class RegisterFragment: HomeBaseFragment() {
    private lateinit var viewModel: RegisterViewModel
    private var isFirst: Boolean = true
    var isFirstLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java)
        showActionBar()
        hideBottomNavView()

        setTitleActionBar(R.string.TEXT_REGISTER)
        initEvent()
    }

    override fun initObserver() {
        viewModel.registerResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                showDialog(R.string.TEXT_REGISTER_SUCCESS)
                navigationController.popToBackStack()
            } else if (it.status == Status.ERROR) {
                showDialog(it.message.toString())
            }
        })
    }

    override fun initEvent() {
        btnRegister.setOnClickListener {
            hideSoftKeyboard()
            if (checkValidRegister()) {
                val data = hashMapOf(
                        "fullName" to edtFullName.text.toString(),
                        "userName" to edtUserName.text.toString(),
                        "email" to edtEmail.text.toString(),
                        "password" to edtPass.text.toString()
                )
                viewModel.requestRegister(data)
            }

        }

        ivRefresh.setOnClickListener {
            captchaImage.regenerate()
        }

        if (isFirst) {
            captchaImage.post(Runnable {
                run {
                    captchaImage.setCaptchaLength(4)
                    captchaImage.setCaptchaType(CaptchaImageView.CaptchaGenerator.NUMBERS)
                    captchaImage.regenerate()
                    isFirst = false
                }
            })
        }
    }

    private fun checkValidRegister(): Boolean {
        return when {
            !checkEmpty(edtFullName) -> {
                showDialog(R.string.TEXT_FULLNAME_NOT_EMPTY)
                false
            }
            !checkEmpty(edtUserName) -> {
                showDialog(R.string.TEXT_USERNAME_NOT_EMPTY)
                false
            }
            !checkEmpty(edtEmail) -> {
                showDialog(R.string.TEXT_EMAIL_NOT_EMPTY)
                false
            }
            !checkValidEmail(edtEmail.text.toString().trim()) -> {
                showDialog(R.string.TEXT_EMAIL_NOT_TRUE)
                false
            }
            !checkEmpty(edtPass) -> {
                showDialog(R.string.TEXT_PASSWORD_NOT_EMPTY)
                false
            }
            !checkSizePassword(edtPass.text.toString().trim()) -> {
                showDialog(R.string.TEXT_PASSWORD_MORE_6_CHAR)
                return false
            }
            !checkEmpty(edtCaptcha) -> {
                showDialog(R.string.TEXT_CAPTCHA_NOT_EMPTY)
                false
            }
            !checkCaptchaImage(edtCaptcha) -> {
                showDialog(R.string.TEXT_CAPTCHA_NOT_TRUE)
                false
            }
            else -> true
        }
    }

    private fun initViews() {
        hideActionBar()
        hideBottomNavView()

    }

    private fun checkCaptchaImage(view: EditText): Boolean {
        if (view.text.toString().trim().equals(captchaImage.captchaCode)) {
            return true
        }
        return false
    }

    private fun checkValidEmail(emai: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(emai).matches()
    }

    private fun checkSizePassword(password: String?) = password?.length ?: 0 >= 6

    private fun checkEmpty(view: EditText): Boolean {
        if (view.text.toString().isBlank()) {
            return false
        }
        return true
    }

    companion object{
        val TAG = "RegisterFragment"
        @JvmStatic
        fun create() = RegisterFragment()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }
}
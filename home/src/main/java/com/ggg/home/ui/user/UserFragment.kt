package com.ggg.home.ui.user

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.StringUtil
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.main.HomeBaseFragment
import io.vrinda.kotlinpermissions.DeviceInfo.Companion.getPackageName
import kotlinx.android.synthetic.main.fragment_user.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import timber.log.Timber
import java.lang.StringBuilder
import java.util.LinkedHashSet

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
        if (GGGAppInterface.gggApp.checkIsLogin()) {
            loginResponse?.let {
                it.user?.let {
                    tvUsername.text = it.fullName
                    if (it.imageUrl.isNotEmpty()) {
                        Glide.with(context!!)
                                .load(it.imageUrl)
                                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(ivAvatar)
                    } else {
                        ivAvatar.setImageResource(R.drawable.i_avatar)
                    }
                }

                llLogout.visibility = View.VISIBLE
                llMyComment.visibility = View.VISIBLE
                llChangePass.visibility = View.VISIBLE
            }
        } else {
            llLogout.visibility = View.GONE
            llMyComment.visibility = View.GONE
            llChangePass.visibility = View.GONE
        }
    }

    override fun initObserver() {
        viewModel.logOutResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                showDialog(R.string.TEXT_LOG_OUT_SUCCESS)
                this.loginResponse = null
                GGGAppInterface.gggApp.loginResponse = null
                GGGAppInterface.gggApp.clearListComicFavorite()
                updateUI()
//            } else if (it.status == Status.ERROR) {
//                showDialog(it.message.toString())
            }
        })
    }

    private fun updateUI() {
        llMyComment.visibility = View.GONE
        llChangePass.visibility = View.GONE
        llLogout.visibility = View.GONE
        tvUsername.text = StringUtil.getString(R.string.TEXT_LOGIN)
        ivAvatar.setImageResource(R.drawable.i_avatar)
    }

    override fun initEvent() {
        llLogin.setOnClickListener {
            if (loginResponse == null) {
                navigationController.showLogin()
            }
        }

        llMyComment.setOnClickListener {
            navigationController.showMyComment()
        }

        llChangePass.setOnClickListener {
            navigationController.showChangePassWord()
        }

        llLogout.setOnClickListener {
            showConfirmDialog(
                    StringUtil.getString(R.string.TEXT_CONFIRM_LOGOUT),
                    "Cancel", DialogInterface.OnClickListener{dialogInterface, _-> dialogInterface.dismiss() },
                    "Yes", DialogInterface.OnClickListener{dialogInterface, _-> run{
                dialogInterface.dismiss()
                var token = ""
                if (loginResponse != null) {
                    token = loginResponse?.tokenType + loginResponse?.accessToken
                }
                val param = hashMapOf(
                        "token" to token
                )
                viewModel.logOut(param)
            }}
            )
        }

        llRate.setOnClickListener {
            goToStore()
        }

        llFeedback.setOnClickListener {
            sendEmail()
        }

        llShareApp.setOnClickListener {
            shareApp()
        }

        llClearCache.setOnClickListener {
            showLoading()
            doAsync {
                viewModel.clearCacheImageDownload()
                Glide.get(context!!).clearDiskCache()
            }

            Glide.get(context!!).clearMemory()

            Handler().postDelayed({
                context?.runOnUiThread { hideLoading() }
            }, 2000)
        }
    }

    private fun goToStore() {
        val appPackageName = getPackageName(context!!) // getPackageName() from Context or Activity object
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName(context!!) + "\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    private fun sendEmail() {
        val to = "heavenmanga2017@gmail.com"
        val subject = "Contact ${getString(R.string.app_name)}"
        val body = "Your feedback will help us make your experience better. Let us know what you think: <br/> <br/> <br/> <br/>" +
                        "--------- <br/> Feedback from Heaven Toon"
        val intent = build(to,subject, body)
        startActivity(intent)
    }

    fun build(to: String, subject: String, body: String): Intent {
        val mailtoUri = constructMailtoUri(to, subject, body)
        return Intent(Intent.ACTION_SENDTO, mailtoUri)
    }

    private fun constructMailtoUri(to: String, subject: String, body: String): Uri {
        val mailto = StringBuilder(1024)
        mailto.append("mailto:")
        val tos = LinkedHashSet<String>()
        tos.add(to)
        addRecipients(mailto, tos)

        var hasQueryParameters = false
        hasQueryParameters = addQueryParameter(mailto, "subject", subject, hasQueryParameters)
        addQueryParameter(mailto, "body", body, hasQueryParameters)

        return Uri.parse(mailto.toString())
    }

    private fun addRecipients(mailto: StringBuilder, recipients: Set<String>) {
        if (recipients.isEmpty()) {
            return
        }

        for (recipient in recipients) {
            mailto.append(encodeRecipient(recipient))
            mailto.append(',')
        }

        mailto.setLength(mailto.length - 1)
    }

    private fun encodeRecipient(recipient: String): String {
        val index = recipient.lastIndexOf('@')
        val localPart = recipient.substring(0, index)
        val host = recipient.substring(index + 1)
        return Uri.encode(localPart) + "@" + Uri.encode(host)
    }

    private fun addQueryParameter(mailto: StringBuilder, field: String, value: String?, hasQueryParameters: Boolean): Boolean {
        if (value == null) {
            return hasQueryParameters
        }

        mailto.append(if (hasQueryParameters) '&' else '?').append(field).append('=').append(Uri.encode(value))

        return true
    }
}
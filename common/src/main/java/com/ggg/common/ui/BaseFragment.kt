package com.ggg.common.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.R
import com.ggg.common.di.Injectable
import com.ggg.common.ui.utils.BaseCellAdapter
import com.ggg.common.ui.utils.BaseInfoCell
import com.ggg.common.ui.utils.BaseInfoCellModel
import com.ggg.common.ui.utils.BaseSectionData
import com.ggg.common.utils.LanguageManager
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.StringUtil
import com.ggg.common.vo.Resource
import com.ggg.common.vo.Status
import com.ggg.common.ws.BaseResponse
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

open class BaseFragment: Fragment(), Injectable, BaseCellAdapter.ItemCellClickListener, OnEventControlListener {

    //region properties

    internal lateinit var adapter: BaseCellAdapter
    val messageEvent = CompositeDisposable()
    lateinit var linearLayoutManager: LinearLayoutManager
    //endregion

    //region lifecycle
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LanguageManager.reloadLanguage(this)
        hideSoftKeyboard()
    }

    fun showActionBar() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showActionBar()
        }
    }

    fun hideActionBar() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).hideActionBar()
        }
    }

    fun showBottomNavView() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showBottomNavView()
        }
    }

    fun hideBottomNavView() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).hideBottomNavView()
        }
    }

    fun setTitleActionBar(title: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).setTitleActionBar(title)
        }
    }

    fun showConfirmDialog(messageId: Int,
                          leftBtnId: Int, leftClick: DialogInterface.OnClickListener,
                          rightBtnId: Int, rightClick: DialogInterface.OnClickListener) {
        showConfirmDialog(StringUtil.getString(messageId),
                    StringUtil.getString(leftBtnId), leftClick, StringUtil.getString(rightBtnId), rightClick)
    }

    fun showConfirmDialog(message: String,
                          leftBtn: String, leftClick: DialogInterface.OnClickListener,
                          rightBtn: String, rightClick: DialogInterface.OnClickListener) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showConfirmDialog(message, leftBtn, leftClick, rightBtn, rightClick)
        }
    }

    fun showDialog(id: Int) {
        showDialog(StringUtil.getString(R.string.TEXT_ANNOUNCE), StringUtil.getString(id))
    }

    fun showDialog(message: String) {
        showDialog(StringUtil.getString(R.string.TEXT_ANNOUNCE), message)
    }

    fun showDialog(title: String, message: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showDialog(title, message)
        }
    }


    fun initAdapter(recycleListView: RecyclerView, list: List<BaseSectionData>? = null, isHideDevice: Boolean = false) {
        adapter = if (list == null) BaseCellAdapter(this) else BaseCellAdapter(list, this)
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycleListView.itemAnimator = DefaultItemAnimator()
        recycleListView.layoutManager = linearLayoutManager
        val itemDecorator = DividerItemDecoration.VERTICAL
        if (!isHideDevice) {
            recycleListView.addItemDecoration(DividerItemDecoration(activity, itemDecorator))
        }
        adapter.shouldShowHeadersForEmptySections(false)
        adapter.shouldShowFooters(false)
        recycleListView.adapter = adapter
    }

    fun updateList(list: List<BaseSectionData>) {
        adapter.dataList = list
        adapter.notifyDataSetChanged()
    }
    //endregion

    //region event
    /**
     * show toast for debug, it will hide when we build release
     *
     * @param msg message
     */
    fun showToastDebug(msg: String) {
        Toast.makeText(activity, msg.toString(), Toast.LENGTH_LONG).show()
    }

    /**
     * show toast for both debug and release
     *
     * @param msg message
     */

    fun showToastRelease(id: Int) {
        Toast.makeText(activity, StringUtil.getString(id), Toast.LENGTH_LONG).show()
    }

    fun showToastRelease(msg: String) {
        Toast.makeText(activity, msg.toString(), Toast.LENGTH_LONG).show()
    }

    /**
     * Hides the soft keyboard
     */
    fun hideSoftKeyboard() {
        val activity = activity
        if (activity != null && activity.currentFocus != null) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager// .getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    fun hideSoftKeyboard(view: View?) {
        val activity = activity
        if (activity != null && view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    /**
     * Shows the soft keyboard
     */
    fun showSoftKeyboard(view: View) {
        val activity = activity
        if (activity != null) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            view.requestFocus()
            inputMethodManager.showSoftInput(view, 0)
        }
    }

    open fun clickBack() {
        Timber.d("Click back from base")
        hideSoftKeyboard()
    }

    fun showLoading(){
        if (activity is BaseActivity){
            (activity as BaseActivity).showLoading()
        }
    }
    fun hideLoading(){
        if (activity is BaseActivity){
            (activity as BaseActivity).hideLoading()
        }
    }
    fun <T> loading(data: Resource<T>){
        if (activity is BaseActivity){
            if (data.status == Status.LOADING) {
                (activity as BaseActivity).showLoading()
            }else if (data.status == Status.SUCCESS || data.status == Status.ERROR){
                (activity as BaseActivity).hideLoading()
            }
        }
    }

    //endregion

    //region support
    fun showMsg(id: Int) {
        Toast.makeText(activity, StringUtil.getString(id), Toast.LENGTH_LONG).show()
    }

    fun showMsg(msg:String) {
        Toast.makeText(activity, msg.toString(), Toast.LENGTH_LONG).show()
    }

    fun showUnderContruction(){
        showMsg("Tính năng này đang được phát triển")
    }

    open fun addRightButton() {

    }

//    fun createButtonTitle(title: String, clickListener: View.OnClickListener): TextView? {
//        val context = context
//        if (context != null) {
//            var text = LanguageManager.getValue(title)
//            val button = TextView(activity)
//            button.text = text
//            button.setTextColor(getContext().resources.getColor(R.color.white))
//            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
//            button.layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT))
//            button.setOnClickListener(clickListener)
//            button.gravity = Gravity.CENTER
//            return button
//        }
//        return null
//    }
//
//    fun createButtonImage(resourceID: Int, clickListener: View.OnClickListener): TextView {
//        val button = TextView(activity)
//        button.background = context.resources.getDrawable(resourceID)
//
//        val lp = LinearLayout.LayoutParams(
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt()
//        )
//        button.layoutParams = lp
//        button.setOnClickListener(clickListener)
//        button.gravity = Gravity.CENTER
//        return button
//    }

//    fun addRightTextButton(text: String, clickListener: View.OnClickListener): TextView? {
//        val button = createButtonTitle(text, clickListener)
//        navigationController.getRightLayout()?.addView(button)
//        return button
//    }
//
//    fun addRightImageButton(resourceID: Int, clickListener: View.OnClickListener): TextView {
//        val button = createButtonImage(resourceID, clickListener)
//        val rightLayout = navigationController.getRightLayout()
//        if (rightLayout != null && rightLayout.getChildCount() > 0) {
//
//
//            val lp = LinearLayout.LayoutParams(
//                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt(),
//                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics).toInt()
//            )
//            lp.setMargins(activity.resources.getDimensionPixelOffset(R.dimen.margin_icon_right), 0, 0, 0)
//            button.layoutParams = lp
//        }
//        navigationController.getRightLayout()?.addView(button)
//        return button
//    }
//
//    fun setStatusResource(resource: LayoutLoadingStateBinding) {
//        resource.setCallback({ resource.setResource(Resource.success(null)) })
//    }

    fun <T> registerProgressBar(progressBar: ProgressBar, resource: Resource<T>) {
        when (resource.status) {
            Status.ERROR, Status.SUCCESS -> progressBar.visibility = View.GONE
            else -> progressBar.visibility = View.VISIBLE
        }
    }

    fun <T> handleResponse(response: Resource<BaseResponse<T>>?
                           , onSuccess: (data: T?) -> Unit = {}
                           , onFailed: (message: Any?) -> Unit = {}) {
        if (null != response) {
            if (response.status!!.equals(Status.SUCCESS)) {
                onSuccess(response.data!!.data)
            } else if (response.status.equals(Status.ERROR)) {
                if (response.message.equals("401")) {
                    Timber.d("response 401")
                } else {
                    onFailed(response.message)
                }
            }
        }
    }

    override fun cellClicked(viewModel: BaseInfoCellModel?, section: Int, position: Int) {
    }

    override fun buttonClicked(cell: BaseInfoCell?, section: Int, position: Int, action: String?) {
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
    }
    //endregion
}
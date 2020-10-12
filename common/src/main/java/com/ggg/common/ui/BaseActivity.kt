package com.ggg.common.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.Window
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.R
import com.ggg.common.ui.utils.BaseCellAdapter
import com.ggg.common.ui.utils.BaseInfoCell
import com.ggg.common.ui.utils.BaseInfoCellModel
import com.ggg.common.ui.utils.BaseSectionData
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.StringUtil
import com.ggg.common.utils.Utils
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import dmax.dialog.SpotsDialog
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.vrinda.kotlinpermissions.PermissionsActivity
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector, BaseCellAdapter.ItemCellClickListener, OnEventControlListener {
    interface PermissionCallBack {

        fun permissionGranted() {
        }

        fun permissionDenied() {
        }
    }
    lateinit var dialog: AlertDialog
    private val registry = LifecycleRegistry(this)
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var compositeDisposable = CompositeDisposable()
    val subjectFreeMemory: Subject<Boolean> = PublishSubject.create()
    override fun getLifecycle(): LifecycleRegistry {
        return registry
    }

    @Inject
    lateinit var dispatch: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatch
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage(" ")
                .setCancelable(false)
                .build()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        compositeDisposable = CompositeDisposable()

        initObserve()
    }

    private fun initObserve() {
        val d = Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    Utils.freeMemory()
                }, {
                    Timber.e(it)
                }, {

                })
        compositeDisposable.add(d)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage(" ")
                .setCancelable(false)
                .build()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        compositeDisposable = CompositeDisposable()
    }

    fun showActionBar() {
        supportActionBar?.show()
    }

    fun hideActionBar() {
        supportActionBar?.hide()
    }

    fun setColorActionBar(color: Int) {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
    }

    fun setColorActionBar(color: String) {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(color)))
    }

    fun setTitleActionBar(title: String) {
        supportActionBar?.title = title.toString()
    }

    fun showConfirmDialog(message: String,
                          leftBtn: String, leftClick: DialogInterface.OnClickListener,
                          rightBtn: String, rightClick: DialogInterface.OnClickListener): Dialog {
        return showConfirmDialog(StringUtil.getString(R.string.TEXT_ANNOUNCE), message, leftBtn, leftClick, rightBtn, rightClick)
    }

    private fun showConfirmDialog(title: String, message: String,
                                  leftBtn: String, leftClick: DialogInterface.OnClickListener,
                                  rightBtn: String, rightClick: DialogInterface.OnClickListener): Dialog {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(rightBtn, rightClick)
        builder.setNegativeButton(leftBtn, leftClick)
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        return dialog
    }

    fun showConfirmDialog(message: String,
                          leftBtn: String, leftClick: DialogInterface.OnClickListener,
                          centerBtn: String, centerClick: DialogInterface.OnClickListener,
                          rightBtn: String, rightClick: DialogInterface.OnClickListener): Dialog {
        return showConfirmDialog(StringUtil.getString(R.string.TEXT_ANNOUNCE), message, leftBtn, leftClick,
                centerBtn, centerClick, rightBtn, rightClick)
    }

    private fun showConfirmDialog(title: String, message: String,
                                  leftBtn: String, leftClick: DialogInterface.OnClickListener,
                                  centerBtn: String, centerClick: DialogInterface.OnClickListener,
                                  rightBtn: String, rightClick: DialogInterface.OnClickListener): Dialog {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNeutralButton(leftBtn, leftClick)
        builder.setNegativeButton(centerBtn, centerClick)
        builder.setPositiveButton(rightBtn, rightClick)
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        return dialog
    }

    fun showDialog(message: String) {
        showDialog(StringUtil.getString(R.string.TEXT_ANNOUNCE), message)
    }

    fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

//    fun showLoading(){
//        if (!dialog.isShowing) {
//            dialog.show()
//        }
//    }

//    fun requestPermisson(arrays: Array<String>, callBack: PermissionCallBack) {
//        requestPermissions(arrays, object: io.vrinda.kotlinpermissions.PermissionCallBack{
//            override fun permissionGranted() {
//                callBack.permissionGranted()
//            }
//            override fun permissionDenied() {
//                callBack.permissionDenied()
//            }
//        })
//    }

    override fun onPause() {
        super.onPause()
        dialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
        compositeDisposable.clear()
    }
//    fun hideLoading(){
//        if (dialog.isShowing) {
//            dialog.dismiss()
//        }
//    }
    fun initAdapter(recycleListView: RecyclerView, listData: MutableLiveData<List<BaseSectionData>>, isHideDevice: Boolean = false, orientation:Int = RecyclerView.VERTICAL) {
        val adapter = BaseCellAdapter(this)
        val linearLayoutManager = LinearLayoutManager(this, orientation, false)
        recycleListView.itemAnimator = DefaultItemAnimator()
        recycleListView.layoutManager = linearLayoutManager
        val itemDecorator = DividerItemDecoration.VERTICAL
        if (!isHideDevice) {
            recycleListView.addItemDecoration(DividerItemDecoration(this, itemDecorator))
        }
//        adapter.shouldShowHeadersForEmptySections(false)
//        adapter.shouldShowFooters(false)
        recycleListView.adapter = adapter
        listData.observe(this, Observer {
            adapter.dataList = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun cellClicked(viewModel: BaseInfoCellModel?, section: Int, position: Int) {
    }

    override fun buttonClicked(cell: BaseInfoCell?, section: Int, position: Int, action: String?) {
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
    }

    open fun showBottomNavView() {
    }

    open fun hideBottomNavView() {
    }

    open fun showScreenById(resourceId: Int) {}

    open fun showLoading() {}
    open fun hideLoading() {}
}
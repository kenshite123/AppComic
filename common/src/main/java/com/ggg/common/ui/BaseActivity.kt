package com.ggg.common.ui

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.ui.utils.BaseCellAdapter
import com.ggg.common.ui.utils.BaseInfoCell
import com.ggg.common.ui.utils.BaseInfoCellModel
import com.ggg.common.ui.utils.BaseSectionData
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import dmax.dialog.SpotsDialog
import io.vrinda.kotlinpermissions.PermissionsActivity
import javax.inject.Inject

open class BaseActivity : PermissionsActivity(), HasSupportFragmentInjector, BaseCellAdapter.ItemCellClickListener {


    interface PermissionCallBack {

        fun permissionGranted() {
        }

        fun permissionDenied() {
        }
    }
    lateinit var dialog: AlertDialog
    private val registry = LifecycleRegistry(this)
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
                .setCancelable(false)
                .build()
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .build()
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
    }
    fun showLoading(){
        if (dialog != null && !dialog.isShowing) {
            dialog.show()
        }
    }

    fun requestPermisson(arrays: Array<String>, callBack: PermissionCallBack) {
        requestPermissions(arrays, object: io.vrinda.kotlinpermissions.PermissionCallBack{
            override fun permissionGranted() {
                callBack.permissionGranted()
            }
            override fun permissionDenied() {
                callBack.permissionDenied()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        dialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
    }
    fun hideLoading(){
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
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
}
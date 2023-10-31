package com.antoco.viewbiding_base.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.antoco.viewbiding_base.dialog.LoadingDialog
import com.blankj.utilcode.util.BarUtils
import org.jetbrains.annotations.NotNull

abstract class BaseActivity<B : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {

    lateinit var binding: B
    var viewModel: VM? = null
    lateinit var mContext:Context
    var loadingDialog: LoadingDialog? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.transparentStatusBar(this)
        BarUtils.setStatusBarLightMode(this, false)

        mContext = this
        binding = initBinding()
        setContentView(binding.root)

        if(createViewModel()!=null){
            viewModel = ViewModelProvider(this)[createViewModel()!!]
            viewModel!!.showLoadingView_event.observe(this){
                if(it){
                    showLoadingView()
                }else{
                    dismissLoadingView()
                }
            }
        }

        initView()
        initData()
    }

    @NotNull
    abstract fun initBinding(): B
    abstract fun createViewModel(): Class<VM>?
    abstract fun initView()
    abstract fun initData()

    fun showLoadingView() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
        loadingDialog!!.show(supportFragmentManager)
    }

    fun dismissLoadingView() {
        if (loadingDialog != null) loadingDialog!!.dismiss()
    }
}
package com.antoco.viewbiding_base.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils

/**********************************
 * @Name:         BaseViewModel
 * @Copyright：  CreYond
 * @CreateDate： 2022/12/12 13:47
 * @author:      HuangFeng
 * @Version：    1.0
 * @Describe:
 *
 **********************************/
open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val showLoadingView_event = MutableLiveData<Boolean>(false)


    fun showLoadingView(){
        showLoadingView_event.postValue(true)
    }

    fun dismissLoadingView(){
        showLoadingView_event.postValue(false)
    }

    fun toast(msg : String){
        ToastUtils.showLong(msg)
    }
}


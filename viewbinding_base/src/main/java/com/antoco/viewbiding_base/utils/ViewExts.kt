package com.antoco.viewbiding_base.utils

import android.view.View
import android.widget.TextView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 5:32 PM
 * @description: View相关扩展
 * @since: 1.0.0
 */

fun <T : View> T.click(action: (T) -> Unit) {
    setOnClickListener {
        action(this)
    }
}

/**d
 * 带有限制快速点击的点击事件
 */
fun <T : View> T.singleClick(interval: Long = 400L, action: ((T) -> Unit)?) {
    setOnClickListener(SingleClickListener(interval, action))
}

class SingleClickListener<T : View>(
    private val interval: Long = 400L,
    private var clickFunc: ((T) -> Unit)?
) : View.OnClickListener {
    private var lastClickTime = 0L

    override fun onClick(v: View) {
        val nowTime = System.currentTimeMillis()
        if (nowTime - lastClickTime > interval) {
            // 单次点击事件
            if (clickFunc != null) {
                clickFunc!!(v as T)
            }
            lastClickTime = nowTime
        }
    }
}

fun TextView.checkNotEmpty(error : String):Boolean{
    val text = text.toString()
    return if(text.isBlank()){
        setError(error)
        false
    }else{
        true
    }
}

inline fun View.multipleClicks(clickTimes : Int, crossinline action: ()-> Unit){
    var count = clickTimes
    var lastClickTime = 0L
    this.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if(currentTime - lastClickTime < 50){//点击间隔过短
        }else if(currentTime - lastClickTime > 800){//点击间隔过长，初始化
            lastClickTime = currentTime
            count = clickTimes-1
        }else{
            lastClickTime = currentTime
            count--
            if(count < 0)return@setOnClickListener
            if(count==0){//点击了最后一次
                action.invoke()
            }
        }
    }
}


fun __123(){

}
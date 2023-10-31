package com.antoco.viewbiding_base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.math.pow
import kotlin.math.sqrt


/**********************************
 * @Name:         Exts
 * @Copyright：  CreYond
 * @CreateDate： 2022/12/14 8:38
 * @author:      HuangFeng
 * @Version：    1.0
 * @Describe:
 *
 **********************************/
fun AppCompatActivity.jumpToWeb(url: String) {
    val intent = Intent()
    intent.action = "android.intent.action.VIEW"
    val content_url: Uri = Uri.parse(url)
    intent.data = content_url
    startActivity(intent)
}

@OptIn(ExperimentalContracts::class)
public inline fun <T> T.nullApply(block: () -> T) : T{
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if(this !=null)return this
    return block()
}


@SuppressLint("SimpleDateFormat")
fun Long.formatTime(): String {
    val hours: Long = TimeUnit.MILLISECONDS.toHours(this)
    val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(this) % 60
//    val formatter = SimpleDateFormat("HH:mm:ss")
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
}


fun isPad(context: Context):Boolean{
    var isPad = (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE

    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val dm = DisplayMetrics()
    display.getMetrics(dm);
    val x = (dm.widthPixels / dm.xdpi).pow(2)
    val y = (dm.heightPixels / dm.ydpi).pow(2)
    val screenInches = sqrt(x + y)// 屏幕尺寸
    return isPad || screenInches >= 7.0
}

/**
 * byte数组转hex
 * @param bytes
 * @return
 */
fun ByteArray.toHexString():String{
    var strHex = ""
    val sb = StringBuilder("")
    for (b in this) {
        strHex = Integer.toHexString(b.toInt() and 0xFF)
        sb.append(if (strHex.length == 1) "0$strHex" else strHex) // 每个字节由两个字符表示，位数不够，高位补0
    }
    return sb.toString().trim()
}

/**
 * byte转hex
 * @param bytes
 * @return
 */
fun Byte.toHexString():String{
    var strHex = Integer.toHexString(this.toInt() and 0xFF)
    return if (strHex.length == 1) "0$strHex" else strHex
}

/**
 * 十六进制字符串转字节码
 *
 * @param b
 * @return
 */
fun String.hex2ByteArray(): ByteArray?{
    val b = this.toByteArray()
    if (b.size % 2 != 0) {
        Log.d(
            "hex2ByteArray",
            "ERROR: 转化失败  le= " + b.size + " b:" + this
        )
        return null
    }
    val b2 = ByteArray(b.size / 2)
    var n = 0
    while (n < b.size) {

        // if(n+2<=b.length){
        val item = String(b, n, 2)
        // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
        b2[n / 2] = item.toInt(16).toByte()
        n += 2
    }
    return b2
}

/**
 * 将string转为固定长度byte数组，不足前面补0
 * @receiver String
 * @param bytesLen Int
 * @return ByteArray
 */
fun String.toBytes(bytesLen : Int,charset: Charset = Charsets.UTF_8):ByteArray{
    val cis = this.toByteArray(charset)
    return if(cis.size > bytesLen){
        this.toByteArray().copyOfRange(0,bytesLen)
    }else{
        val bs = ByteArray(bytesLen)
        cis.copyInto(bs,bytesLen-cis.size,0,cis.size)
    }
}

fun Int.toBigByteArray2():ByteArray{
    return byteArrayOf((this shr 8).toByte(), (this and 0xFF).toByte())
}

fun Int.toBigByteArray4():ByteArray{
    return byteArrayOf((this shr 24).toByte(), (this shr 16).toByte(),(this shr 8).toByte(),(this and 0xFF).toByte())
}

fun toBigUInt(b1:Byte, b2: Byte):Int{
    return (b1.toInt() and 0xff shl 8) or (b2.toInt() and 0xff)
}

fun toBigInt(b1:Byte, b2: Byte):Int{
    return (b1.toInt()  shl 8) or (b2.toInt() )
}

fun toBigInt(b1:Byte, b2: Byte,b3:Byte, b4: Byte):Int{
    return ((b1.toInt() and 0xff)shl 24) or
            ((b2.toInt() and 0xff) shl 16) or
            ((b3.toInt() and 0xff) shl 8) or
            ((b4.toInt() and 0xff))
}


operator fun FloatArray.plus(floatArray: FloatArray):FloatArray{
    val dest = FloatArray(this.size + floatArray.size)
    this.copyInto(dest,0,0,this.size)
    floatArray.copyInto(dest,this.size+1,dest.size)
    return dest
}

operator fun FloatArray.plus(last: Int):FloatArray{
    val dest = FloatArray(this.size + 1)
    this.copyInto(dest,0,0,this.size)
    dest[dest.size-1] = last.toFloat()
    return dest
}
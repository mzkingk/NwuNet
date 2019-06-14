package com.shuaiqing.nwunet.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import java.io.IOException


object Net {
    /**
     * 判断 WiFi 连接
     */
    fun isWiFiConnected(context: Context): Boolean {
        val manager: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo: NetworkInfo? = manager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    //判断网络是否连接(是否打开移动网络或者是连接wifi)
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isConnected
            }
        }
        return false
    }

    //判断外网是否可用（常用于wifi已经连接但是无法访问外网的情况），耗时操作，不应该放在主线程
    fun isNetworkOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ip = "www.baidu.com"// 除非百度挂了，否则用这个应该没问题~
            val ipProcess = runtime.exec("ping -c 3 -w 100 $ip")//ping3次
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}

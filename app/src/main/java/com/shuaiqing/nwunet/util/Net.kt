package com.shuaiqing.nwunet.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object Net {
    /**
     * 判断 WiFi 连接
     */
    fun isWiFiConnected(context: Context): Boolean {
        val manager: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo: NetworkInfo? = manager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isAvailable
                && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }
}

package com.shuaiqing.nwunet.util

import android.os.Build

/**
 * 全局变量,地址,账户密码
 */
object C {
    val SDK = Build.VERSION.SDK_INT
    val LOG_TAG = "ONE_TAP_CDUT_NET"

    // 校园网登录地址
    val CAMPUS_NET_URL = "http://10.0.1.242/a70.htm"

    val CAMPUS_CHECK_URL ="http://10.0.1.242"

    // 登录用户名
    val USER_NAME = "***"
    // 登录密码
    val PWD = "***"
}
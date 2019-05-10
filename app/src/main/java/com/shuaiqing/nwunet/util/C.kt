package com.shuaiqing.nwunet.util

import java.lang.StringBuilder
import java.util.*

/**
 * 全局变量,地址,账户密码
 */
object C {
    val LOG_TAG = "ONE_TAP_CDUT_NET"

    // 多个不同登录地址,旧的nwunet
    val CAMPUS_CHECK_URL = "http://10.0.1.242"
    val CAMPUS_CHECK_URL2 = "http://10.0.1.237"

    //新的,newnwu
    val CAMPUS_CHECK_URL3 = "http://s3.cn-northwest-1.amazonaws.com.cn/captive-portal/connection-test.html"
    val CAMPUS_CHECK_URL4 = "http://10.16.0.11/web/connect"

    val CAMPUS_RANDOM = StringBuilder("?random=" + Date().time.toString() + (Math.random() * 100000).toInt())
}
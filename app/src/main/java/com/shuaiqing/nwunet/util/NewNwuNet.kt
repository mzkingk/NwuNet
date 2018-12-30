package com.shuaiqing.nwunet.util

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.*

object NewNwuNet {
    /* 检测校园网连接状态
     * @return null  - 未连接到校园网
     *         false - 已连接校园网但未登录
     *         true  - 已登录校园网
     */
    fun check(checkUrl: String, random: StringBuilder?): String? {
        Log.d(C.LOG_TAG, "NewNwuNet - check")
        try {
            val obj = URL(if (random == null) checkUrl else checkUrl + random)
            val response = StringBuffer()
            with(obj.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                connectTimeout = 600
                readTimeout = 600
                setRequestProperty("content-type", "application/javascript")
                BufferedReader(InputStreamReader(inputStream, "GBK")).use {
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                }
            }
//            println("response->"+response)
            if (response.contains("Success")) {     //返回给service的
                return "200"
            } else {    //返回给login的
                var st = response.toString().indexOf("location.href") + 15
                var end = response.toString().indexOf("/script") - 2
                return response.substring(st, end)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 登录校园网
     * @param account 登录帐号
     * @param passwd 登录密码
     * @return true - 登录成功
     *          false - 登录失败
     */
    fun login(account: String, passwd: String): Boolean? {
        Log.d(C.LOG_TAG, "NewNwuNet-login")
        try {
            var redir: String? = null
            for (i in check(C.CAMPUS_CHECK_URL3, C.CAMPUS_RANDOM).toString()) {
                redir += if (i == '&') "%26" else i
            }
            var formData3 = "web-auth-user=$account&web-auth-password=$passwd&remember-credentials=false&redirect-url=" + redir
            val con = URL(C.CAMPUS_CHECK_URL4).openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8")
            var data = DataOutputStream(con.outputStream)
            data.write(formData3.toByteArray())
            data.flush()
            data.close()
            println("res->\n"+con.responseCode)
            return if (con.responseCode == 200) true else false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

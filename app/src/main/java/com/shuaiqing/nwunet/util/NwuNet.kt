package com.shuaiqing.nwunet.util

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NwuNet {
    /* 检测校园网连接状态
     * @return null  - 未连接到校园网
     *         false - 已连接校园网但未登录
     *         true  - 已登录校园网
     */
    fun check(checkUrl :String): Boolean? {
        Log.d(C.LOG_TAG, "NwuNet - check")
        try {
            val obj = URL(checkUrl)
            val response = StringBuffer()
            with(obj.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                connectTimeout = 600
                readTimeout = 600
                BufferedReader(InputStreamReader(inputStream, "GB2312")).use {
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                }
            }
            return response.contains("已使用时间")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 登录校园网
     * @param checkUrl 地址
     * @param userName 登录帐号
     * @param pwd 登录密码
     * @return true - 登录成功
     *          false - 登录失败
     */
    fun login(checkUrl: String,userName: String, pwd: String): Boolean {

        Log.d(C.LOG_TAG, "NwuNet - login")
        try {
            val huc = URL(checkUrl).openConnection() as HttpURLConnection
            huc.requestMethod = "POST"
            huc.connectTimeout = 4000
            huc.readTimeout = 4000
            huc.doOutput = true
            //formdata,各个学校的都不一样
            val paras = "DDDDD=$userName&upass=$pwd&R1=0&R2=&R6=0&para=00&0MKKey=123456&v6ip=&hid1=4458&hid2=52030005&cn=2"
            val dos = DataOutputStream(huc.outputStream)
            dos.write(paras.toByteArray())
            dos.flush()
            dos.close()

            val inputStream = huc.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, "GBK"))
            val sbContent = StringBuilder()
            var buffer = bufferedReader.readLine()
            while (buffer != null) {
                sbContent.append(buffer)
                buffer = bufferedReader.readLine()
            }
            bufferedReader.close()
            return sbContent.contains("登录成功")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}

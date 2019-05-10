package com.shuaiqing.nwunet.service

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.shuaiqing.nwunet.R
import com.shuaiqing.nwunet.util.C
import com.shuaiqing.nwunet.util.NwuNet
import com.shuaiqing.nwunet.util.Net
import com.shuaiqing.nwunet.util.NewNwuNet


/**
 * service层
 */
class QSTileService : TileService() {
    /* 图块可见回调*/
    override fun onStartListening() {
        super.onStartListening()
        if (Net.isWiFiConnected(this)) {
            updateStatus(R.string.tile_label)
        }
        println(Net.isWiFiConnected(this))
//        else { // 未连接 WiFi 则将图块置为不可用状态
//            updateStatus(R.string.tile_status_no_net_conn, true)
//        }
    }

    /**
     * 图块点击回调
     */
    override fun onClick() {
        super.onClick()
        Task().execute()
    }

    /**
     * 核心异步任务，检查、登录校园网
     */
    @SuppressLint("StaticFieldLeak")
    inner class Task : AsyncTask<String, Int, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            publishProgress(R.string.tile_status_check)
            var res = NwuNet.check(C.CAMPUS_CHECK_URL) // 检查242校园网连接
            println("检查校园网连接old->" + res)

            var flagNew = 0     //0为非新校园网,1为新校园网但未登录,2为已登录
            if (res == null || res == false) {
                val resultNewNet = NewNwuNet.check(C.CAMPUS_CHECK_URL3, null) //检查新校园网的地址
                flagNew = if (resultNewNet != null && resultNewNet.contains("200")) 2 else 1
                if (flagNew == 0) {
                    res = null
                } else {
                    res = if (flagNew == 2) true else false
                }
            }
            println("检查校园网连接new->" + res)

            if (res == null) {// 未连接校园网，直接结束
                publishProgress(R.string.tile_status_not)
                return false
            } else if (!res) {// 未登录校园网，进行登录操作
                publishProgress(R.string.tile_status_loggin)
                //缓存中读取数据
                val loginData = getSharedPreferences("loginData", Context.MODE_PRIVATE)
                val account = loginData.getString("account", "123456")
                val passwd = loginData.getString("passwd", "empty")
                if (!NwuNet.login(C.CAMPUS_CHECK_URL, account, passwd)) { // 旧网242登录失败 ? 登录新网 : true
                    if (flagNew == 0) {
                        publishProgress(R.string.tile_status_failed)
                        return false    //当前是旧校园网,但是登录失败
                    }
                    val loginDataNwu = getSharedPreferences("loginDataNwu", Context.MODE_PRIVATE)
                    val accountNew = loginDataNwu.getString("account", "2015110110")
                    val passwdNew = loginDataNwu.getString("passwd", "empty")
                    if (NewNwuNet.login(accountNew, passwdNew) != true) { // 尝试新校园网
                        publishProgress(R.string.tile_status_conflict)
                        return false
                    }
                }
            }
            // 如果能运行到这里,表示已登录
            publishProgress(R.string.tile_status_ok)
            return true
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            updateStatus(values[0])
        }
    }

    /**
     * 更新图块状态
     *
     * @param labelId 文字ID
     * @param enable 状态
     */
    fun updateStatus(labelId: Int?, enable: Boolean = true) {
        try {
            if (qsTile != null) {
                if (labelId != null) {
                    qsTile.label = getString(labelId)
                }
                if (enable) {
                    qsTile.state = Tile.STATE_ACTIVE
                } else {
                    qsTile.state = Tile.STATE_UNAVAILABLE
                }
                qsTile.updateTile()
            }
        } catch (e: Exception) {
            println("更新图块抛出异常")
        }
    }
}

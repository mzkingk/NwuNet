package com.shuaiqing.nwunet.service

import android.content.Context
import android.os.AsyncTask
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.shuaiqing.nwunet.R
import com.shuaiqing.nwunet.util.C
import com.shuaiqing.nwunet.util.C.CAMPUS_CHECK_URL2
import com.shuaiqing.nwunet.util.C.CAMPUS_CHECK_URL3
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
        } else { // 未连接 WiFi 则将图块置为不可用状态
            updateStatus(R.string.tile_status_no_net_conn, false)
        }
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
    inner class Task : AsyncTask<String, Int, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            publishProgress(R.string.tile_status_check)
            var res = NwuNet.check(C.CAMPUS_CHECK_URL) // 检查242校园网连接
            println("检查校园网连接1" + res)
            if (res == null || res == false) {
                res = NwuNet.check(CAMPUS_CHECK_URL2) //检查237地址
            }
            println("检查校园网连接2" + res)
            if (res == null || res == false) {
                var resultNewNet = NewNwuNet.check(CAMPUS_CHECK_URL3, null) //检查新校园网的地址
                var flagNew = if (resultNewNet != null && resultNewNet.contains("200")) 2 else 1        //0为非新校园网,1为新校园网但未登录,2为已登录
                if (flagNew == 0) {
                    res = null
                } else {
                    res = if (flagNew == 2) true else false
                }
            }
            println("检查校园网连接3" + res)

            if (res == null) {// 未连接校园网，直接结束
                publishProgress(R.string.tile_status_not)
                return false
            } else if (!res) {// 未登录校园网，进行登录操作
                publishProgress(R.string.tile_status_loggin)
                //缓存中读取数据
                val loginData = getSharedPreferences("loginData", Context.MODE_PRIVATE)
                val account = loginData.getString("account", "2015110110")
                val passwd = loginData.getString("passwd", "empty")
                if (!NwuNet.login(C.CAMPUS_CHECK_URL, account, passwd)) { // 尝试242
                    if (!NwuNet.login(CAMPUS_CHECK_URL2, account, passwd)) { // 尝试237
                        val loginDataNwu = getSharedPreferences("loginDataNwu", Context.MODE_PRIVATE)
                        val accountNew = loginDataNwu.getString("account", "2015110110")
                        val passwdNew = loginDataNwu.getString("passwd", "empty")
                        if (!NewNwuNet.login(accountNew, passwdNew)) { // 尝试新校园网
                            publishProgress(R.string.tile_status_failed)
                            return false    //三种情况全失败
                        }
                    }
                }
            }
            // 如果能运行到这里,res==true,表示已登录
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
}

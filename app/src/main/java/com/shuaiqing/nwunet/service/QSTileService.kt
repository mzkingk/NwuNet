package com.shuaiqing.nwunet.service

import android.content.Context
import android.os.AsyncTask
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.shuaiqing.nwunet.R
import com.shuaiqing.nwunet.util.NwuNet
import com.shuaiqing.nwunet.util.Net


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
            val res = NwuNet.check() // 检查校园网连接
            println("检查校园网连接" + res)
            // 未连接校园网，直接结束
            if (res == null) {
                publishProgress(R.string.tile_status_not)
                return false
            }
            // 未登录校园网，进行登录操作
            else if (!res) {
                publishProgress(R.string.tile_status_loggin)

                //缓存中读取数据
                val preferences = getSharedPreferences("loginData", Context.MODE_PRIVATE)
                val account = preferences.getString("account", "2015110110")
                val passwd = preferences.getString("passwd", "empty")

                if (!NwuNet.login(account, passwd)) { // 登录失败，结束
                    publishProgress(R.string.tile_status_failed)
                    return false
                }
            }
            // res==true，表示已登录
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

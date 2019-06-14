package com.shuaiqing.nwunet.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.shuaiqing.nwunet.R
import com.shuaiqing.nwunet.service.QSTileService
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri


class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextAccount = findViewById(R.id.editTextAccount) as EditText
        val editTextPasswd = findViewById(R.id.editTextPasswd) as EditText

        //读取缓存的账号密码
        getDataBtn.setOnClickListener {
            val data = getSharedPreferences("loginData", Context.MODE_PRIVATE)
            val account = data.getString("account", "空")
            val passwd = data.getString("passwd", "-1")

            val dataNew = getSharedPreferences("loginDataNwu", Context.MODE_PRIVATE)
            val accountNew = dataNew.getString("account", "空")
            val passwdNew = dataNew.getString("passwd", "-1")
            Toast.makeText(this, "当前缓存的\n旧账号" + account + ",密码长度" + passwd.length + "\n新账号" + accountNew + ",密码长度" + passwdNew.length, Toast.LENGTH_SHORT).show()
        }

        //保存旧账号
        BtnSetOld.setOnClickListener {
            val account = editTextAccount.text.toString()
            val passwd = editTextPasswd.text.toString()
            if (account.length > 5 && passwd.length > 5 && passwd.length <= 20) {
                val data = getSharedPreferences("loginData", Context.MODE_PRIVATE)
                data.edit().putString("account", editTextAccount.text.toString()).putString("passwd", editTextPasswd.text.toString()).apply()
                //toast
                Toast.makeText(this, "成功将" + editTextAccount.text + "放入缓存", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "账号或密码不合理,请检查", Toast.LENGTH_SHORT).show()
            }
        }
        //保存新账号
        BtnSetNew.setOnClickListener {
            val account = editTextAccount.text.toString()
            val passwd = editTextPasswd.text.toString()
            if (account.length > 5 && passwd.length > 5 && passwd.length <= 20) {
                val data = getSharedPreferences("loginDataNwu", Context.MODE_PRIVATE)
                data.edit().putString("account", editTextAccount.text.toString()).putString("passwd", editTextPasswd.text.toString()).apply()
                Toast.makeText(this, "成功将" + editTextAccount.text + "放入缓存", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "账号或密码不合理,请检查", Toast.LENGTH_SHORT).show()
            }
        }
        //登录按钮
        BtnLogin.setOnClickListener {
            QSTileService().onClick()
        }

        BtnWeb.setOnClickListener {
            Toast.makeText(this, "正在跳转至浏览器登录", Toast.LENGTH_SHORT).show()
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://10.16.0.12:8081/login"))
                startActivity(intent)
            } catch (e: Exception) {
                println("抛出异常")
            }
        }
    }
}

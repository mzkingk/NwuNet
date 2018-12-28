package com.shuaiqing.nwunet.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.shuaiqing.nwunet.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var editTextAccount = findViewById(R.id.editTextAccount) as EditText
        var editTextPasswd = findViewById(R.id.editTextPasswd) as EditText

        //读取缓存的账号密码
        getDataBtn.setOnClickListener {
            val data = getSharedPreferences("loginData", Context.MODE_PRIVATE)
            val account = data.getString("account", "空")
            val passwd = data.getString("passwd", "-1")
            val dataNew = getSharedPreferences("loginDataNwu", Context.MODE_PRIVATE)
            val accountNew = dataNew.getString("account", "空")
            val passwdNew = dataNew.getString("passwd", "-1")
            Toast.makeText(this, "当前缓存的\n旧账号" + account + ",密码长度" + passwd.length+"\n新账号"+accountNew+",密码长度"+passwdNew.length, Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, Date().time.toString()+(Math.random() * 1000000).toInt(), Toast.LENGTH_SHORT).show()
        }

        //保存旧账号
        submitBtn.setOnClickListener {
            var account = editTextAccount.text.toString()
            var passwd = editTextPasswd.text.toString()
            if (account.length > 5 && passwd.length > 5 && passwd.length <= 20) {
                val data = getSharedPreferences("loginData", Context.MODE_PRIVATE)
                data.edit().putString("account", editTextAccount.text.toString()).putString("passwd", editTextPasswd.text.toString()).commit()
                //toast
                Toast.makeText(this, "成功将" + editTextAccount.text + "放入缓存", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "账号或密码不合理,请检查", Toast.LENGTH_SHORT).show()
            }
        }
        //保存新账号
        submitBtnNew.setOnClickListener {
            var account = editTextAccount.text.toString()
            var passwd = editTextPasswd.text.toString()
            if (account.length > 5 && passwd.length > 5 && passwd.length <= 20) {
                val data = getSharedPreferences("loginDataNwu", Context.MODE_PRIVATE)
                data.edit().putString("account", editTextAccount.text.toString()).putString("passwd", editTextPasswd.text.toString()).commit()
                //toast
                Toast.makeText(this, "成功将" + editTextAccount.text + "放入缓存", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "账号或密码不合理,请检查", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

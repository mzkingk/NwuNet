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

        getDataBtn.setOnClickListener {
            val data = getSharedPreferences("loginData", Context.MODE_PRIVATE)
            val account = data.getString("account", "empty")
            val passwd = data.getString("passwd", "empty")
            Toast.makeText(this, "当前缓存的账号为" + account + ",密码长度为" + passwd.length, Toast.LENGTH_SHORT).show()
        }

        submitBtn.setOnClickListener {
            var account = editTextAccount.text.toString()
            var passwd = editTextPasswd.text.toString()
            if (account.length == 10 && passwd.length >= 6 && passwd.length <= 20) {
                //放入缓存
                val data = getSharedPreferences("loginData", Context.MODE_PRIVATE)
                data.edit().putString("account", editTextAccount.text.toString()).putString("passwd", editTextPasswd.text.toString()).commit()
                //toast
                Toast.makeText(this, "成功将" + editTextAccount.text + "放入缓存", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "账号或密码不合理,请检查", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

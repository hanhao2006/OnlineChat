package com.haohan.onlinechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.haohan.onlinechat.DB.LoginAccess
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var login = LoginAccess()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: Toolbar = findViewById(R.id.toolbar_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val co = this.applicationContext

        btn_login.setOnClickListener {
            login.login(login_email.text.toString(),login_password.text.toString(),co)
            Toast.makeText(this,"login.....",Toast.LENGTH_SHORT).show()
        }

    }
}

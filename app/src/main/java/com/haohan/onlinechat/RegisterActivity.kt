package com.haohan.onlinechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.haohan.onlinechat.DB.RegisterAccess
import com.haohan.onlinechat.modle.Users
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private var RegisterAccess = RegisterAccess()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var context = this.applicationContext

        val toolbar: Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registration"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        register_signup.setOnClickListener {
            RegisterAccess.regsiter(register_email.text.toString(),register_password.text.toString(),register_name.text.toString(),context)
        }

    }
}

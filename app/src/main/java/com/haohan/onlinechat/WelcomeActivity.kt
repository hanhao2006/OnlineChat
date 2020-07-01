package com.haohan.onlinechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity(),View.OnClickListener {

    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btn_login.setOnClickListener(this)
        btn_register.setOnClickListener(this)


    }

    override fun onStart() {
        super.onStart()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        if(firebaseUser !=null){
            startActivity(Intent(this@WelcomeActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
                R.id.btn_login -> startActivity(Intent(this@WelcomeActivity,LoginActivity::class.java))
                R.id.btn_register -> startActivity(Intent(this@WelcomeActivity,RegisterActivity::class.java))
                else -> "do some thing"
        }
    }

}

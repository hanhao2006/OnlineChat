package com.haohan.onlinechat.DB

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.haohan.onlinechat.MainActivity

class LoginAccess {

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private fun checkIsEmpty(email: String, password: String): Boolean{
        return when {
            email == "" -> {
                false
            }
            password == "" -> {
                false
            }
            else -> true
        }
    }

    fun login(email: String, password: String,context: Context){
        var check = checkIsEmpty(email,password)
        if(!check){
            Toast.makeText(context,"Please check your input...", Toast.LENGTH_SHORT).show()
        }else{
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        context.startActivity(Intent(context,MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                    }else{
                        Toast.makeText(context,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
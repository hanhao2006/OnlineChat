package com.haohan.onlinechat.DB

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.haohan.onlinechat.MainActivity
import com.haohan.onlinechat.modle.Users

class RegisterAccess{

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var refUser: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

    private fun checkIsEmpty(email: String, password: String, userName: String): Boolean{
        return when {
            userName == "" -> {
                false
            }
            email == "" -> {
                false
            }
            password == "" -> {
                false
            }
            else -> true
        }
    }

    fun regsiter(email: String, password: String, userName: String,context: Context){
        var check = checkIsEmpty(email,password,userName)
        if(!check){
            Toast.makeText(context,"Please check your input...",Toast.LENGTH_SHORT).show()
        }else{

            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        updates(userName,context)
                    }else{
                        Toast.makeText(context,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updates(userName: String,context: Context){
        var firebaseUserId: String = mAuth.currentUser!!.uid
        var user = Users(firebaseUserId, userName,"https://firebasestorage.googleapis.com/v0/b/onlinechat-447b3.appspot.com/o/profile.png?alt=media&token=529b3b65-9032-44c8-b309-062238265533",
            "https://firebasestorage.googleapis.com/v0/b/onlinechat-447b3.appspot.com/o/cover_defult.jpeg?alt=media&token=d0bd3bf2-bc93-4b7f-b306-7cbae5e36c87",
            "offline",userName.toLowerCase(),"https//m.facebook.com","https//m.instagram.com","https//www.google.com")
        refUser.child(firebaseUserId).setValue(user)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    context.startActivity(Intent(context,MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }

    }


}
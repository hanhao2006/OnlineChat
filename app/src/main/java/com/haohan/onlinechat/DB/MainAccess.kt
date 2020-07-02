package com.haohan.onlinechat.DB

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.haohan.onlinechat.modle.Users
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MainAccess {
    var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    var refUsers : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

    fun getUserNameAndProfile(textView: TextView, profileImage: CircleImageView, id: Int){
        refUsers.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user: Users?= p0.getValue(Users::class.java)
                    textView.text = user!!.username
                    Picasso.get().load(user.profile).placeholder(id).into(profileImage)
                }
            }

        })
    }
}


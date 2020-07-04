package com.haohan.onlinechat.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.haohan.onlinechat.R
import com.haohan.onlinechat.modle.Users
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_setting.view.*

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : Fragment() {

    var mAuth: FirebaseUser? = null
    var databaseRef: DatabaseReference? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       val view = inflater.inflate(R.layout.fragment_setting, container, false)
        mAuth = FirebaseAuth.getInstance().currentUser
        databaseRef = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth!!.uid)

        databaseRef!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user: Users? = p0.getValue(Users::class.java)
                    if(context != null){
                        view.userName_setting.text = user!!.username
                        Picasso.get().load(user!!.profile).into(view.setting_circleView)
                        Picasso.get().load(user!!.cover).into(view.cover_image)


                    }
                }
            }

        })

        return view
    }

}

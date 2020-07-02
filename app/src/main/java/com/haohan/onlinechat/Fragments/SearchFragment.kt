package com.haohan.onlinechat.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.haohan.onlinechat.R
import com.haohan.onlinechat.adapter.UserAdapter
import com.haohan.onlinechat.modle.Users
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null
    private var recyclerView: RecyclerView? = null
    private var edit_search: EditText? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.search_list)
        edit_search = view.findViewById(R.id.edit_search)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        mUsers = ArrayList()
        retrieveAllUsers()

        edit_search!!.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchUsers(p0.toString().toLowerCase())
            }

        })
        return view
    }


    private fun retrieveAllUsers() {
        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        var refUsers = FirebaseDatabase.getInstance().reference.child("Users")
        refUsers.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                if(edit_search!!.text.toString() == ""){

                    for(dataSnapshot in p0.children ){
                        val user: Users? = dataSnapshot.getValue(Users::class.java)
                        if(user!!.uid != userid){
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }
                }

                userAdapter = UserAdapter(context!!,mUsers!!,false)
                recyclerView!!.adapter = userAdapter
            }

        })
    }

    private fun searchUsers(str: String){
        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        var queryUsers = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search").startAt(str)
            .endAt(str + "\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                (mUsers as ArrayList<Users>).clear()
                for(dataSnapshot in p0.children ){
                    val user: Users? = dataSnapshot.getValue(Users::class.java)
                    if(user!!.uid != userid){
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }
                userAdapter = UserAdapter(context!!,mUsers!!,false)
                recyclerView!!.adapter = userAdapter

            }

        })

    }

}

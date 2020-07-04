package com.haohan.onlinechat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haohan.onlinechat.DB.MainAccess
import com.haohan.onlinechat.R
import com.haohan.onlinechat.modle.Users
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    context: Context,
    users: List<Users>,
    isChatCheck: Boolean

  ): RecyclerView.Adapter<UserAdapter.ViewHolder?>() {


    private val context: Context = context
    private val users: List<Users> = users
    private var isChatCheck: Boolean = isChatCheck

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(context).inflate(R.layout.search_user,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      var user: Users = users[position]
        holder.userName.text = user!!.username
        Picasso.get().load(user.profile).placeholder(R.drawable.ic_profile).into(holder.profileImage)

    }


    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        var userName: TextView = itemView.findViewById(R.id.username_search)
        var profileImage: CircleImageView = itemView.findViewById(R.id.profile_image_search)
        var onlineSign: CircleImageView = itemView.findViewById(R.id.sign_online)
        var offlineSign: CircleImageView = itemView.findViewById(R.id.sign_online)
        var lastSeen: TextView = itemView.findViewById(R.id.message_last)
    }

}


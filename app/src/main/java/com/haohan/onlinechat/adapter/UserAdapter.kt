package com.haohan.onlinechat.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haohan.onlinechat.DB.MainAccess
import com.haohan.onlinechat.MessageChatActivity
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
        holder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence>(
                "Send Message",
                "Visit Profile"
            )

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("What do you want?")
            builder.setItems(options,DialogInterface.OnClickListener { dialogInterface, i ->
                if(i  == 0 ){
                    val intent = Intent(context,MessageChatActivity::class.java)
                    intent.putExtra("receiverId",user.uid)
                    context.startActivity(intent)
                }
                if(i == 1){

                }
            })
            builder.show()
        }
    }


    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        var userName: TextView = itemView.findViewById(R.id.username_search)
        var profileImage: CircleImageView = itemView.findViewById(R.id.profile_image_search)
        var onlineSign: CircleImageView = itemView.findViewById(R.id.sign_online)
        var offlineSign: CircleImageView = itemView.findViewById(R.id.sign_online)
        var lastSeen: TextView = itemView.findViewById(R.id.message_last)
    }

}


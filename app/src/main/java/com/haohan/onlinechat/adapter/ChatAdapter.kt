package com.haohan.onlinechat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.haohan.onlinechat.R
import com.haohan.onlinechat.modle.Chat
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(
    mContext: Context,
    mChatList: List<Chat>,
    imageUrl: String

): RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    private  val mContext:Context
    private  val mChatList: List<Chat>
    private  val imageUrl: String

    var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    init {
        this.mChatList = mChatList
        this.mContext = mContext
        this.imageUrl = imageUrl
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val chat: Chat = mChatList[position]
        Picasso.get().load(imageUrl).into(holder.profile_image)

        //image message
        if(chat.message.equals("send you an image") && !chat.url.equals("")) {


            //image message - right side
            if(chat.sender.equals(firebaseUser!!.uid)){

                holder.show_text_message!!.visibility = View.GONE
                holder.right_image_view!!.visibility = View.GONE
                Picasso.get().load(chat.url).into(holder.right_image_view)

            }else if(!chat.sender.equals(firebaseUser!!.uid)){

                holder.show_text_message!!.visibility = View.GONE
                holder.left_image_view!!.visibility = View.GONE
                Picasso.get().load(chat.url).into(holder.left_image_view)
            }
        }
        // text message
        else{
            holder.show_text_message!!.text = chat.message
        }
        // sent and see message
        if(position == mChatList.size - 1){
           if(chat.isseen){
               holder.text_seen_item_left!!.text = "Seen"
               if(chat.message.equals("send you an image") && !chat.url.equals("")){
                   val relativeLayout: RelativeLayout.LayoutParams? = holder.text_seen_item_left!!.layoutParams as RelativeLayout.LayoutParams
                   relativeLayout!!.setMargins(0,245,10,0)
                   holder.text_seen_item_left!!.layoutParams = relativeLayout
               }

           }else{
               holder.text_seen_item_left!!.text = "Sent"
               if(chat.message.equals("send you an image") && !chat.url.equals("")){
                   val relativeLayout: RelativeLayout.LayoutParams? = holder.text_seen_item_left!!.layoutParams as RelativeLayout.LayoutParams
                   relativeLayout!!.setMargins(0,245,10,0)
                   holder.text_seen_item_left!!.layoutParams = relativeLayout
               }

           }

        }else{
            holder.text_seen_item_left!!.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {

        return if(position == 1){
            val view: View = LayoutInflater.from(mContext).inflate(R.layout.message_item_right_,parent,false)
            ViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(mContext).inflate(R.layout.message_item_left,parent,false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mChatList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profile_image: CircleImageView? = null
        var show_text_message: TextView? = null
        var left_image_view: ImageView? = null
        var text_seen_item_left: TextView? = null
        var right_image_view: ImageView? = null

        init {
            profile_image = itemView.findViewById(R.id.profile_image_message_item)
            show_text_message = itemView.findViewById(R.id.show_text_message)
            left_image_view = itemView.findViewById(R.id.left_image_view)
            text_seen_item_left = itemView.findViewById(R.id.text_seen_item_left)
            right_image_view = itemView.findViewById(R.id.right_image_view)
        }
    }

    override fun getItemViewType(position: Int): Int {

        return  if(mChatList[position].sender.equals(firebaseUser!!.uid)){
            1
        }else{
            0
        }
    }
}
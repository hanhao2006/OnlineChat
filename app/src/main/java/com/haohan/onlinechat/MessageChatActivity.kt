package com.haohan.onlinechat


import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.haohan.onlinechat.adapter.ChatAdapter
import com.haohan.onlinechat.modle.Chat
import com.haohan.onlinechat.modle.Users
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*


class MessageChatActivity : AppCompatActivity() {

    var receiverId : String? = ""
    var senderId: FirebaseUser? = null
    var chatAdapter: ChatAdapter? = null
    var mChatList: List<Chat>? = null
    lateinit var recycler_view_chats: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        // get id which will receive message from user adapter
        receiverId = intent.getStringExtra("receiverId")

        // get id which will send message from currentUser of firebaseAuth
        senderId = FirebaseAuth.getInstance().currentUser

        recycler_view_chats = findViewById(R.id.recylerView_messageChat)
        recycler_view_chats.setHasFixedSize(true)

        var linerLayoutMessage = LinearLayoutManager(applicationContext)
        linerLayoutMessage.stackFromEnd = true
        recycler_view_chats.layoutManager = linerLayoutMessage

        // create a firebaseDatabase object to get receiver name and profile to display on the text view
        val reference = FirebaseDatabase.getInstance().reference
            .child("Users").child(receiverId!!)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // do something
            }

            override fun onDataChange(p0: DataSnapshot) {
                // create a user object to get value from users table
                val user: Users? = p0.getValue(Users::class.java)
                username_messageChat.text = user!!.username
                Picasso.get().load(user.profile).into(profile_image_messageChat)

                retrieveMessage(senderId!!.uid, receiverId, user.profile)
            }

        })

        //listener the bottom of send message
        sendMessage_button_messageChat.setOnClickListener {

            // get message from the sender who want to sent to message to friend
            var message = messageSend_messageChat.text.toString()

            if (message == null) {
                Toast.makeText(
                    this@MessageChatActivity,
                    "Please write down you message to send...",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                // create a function to send message to user, in the same have to upload data to database
                sendMessageToUser(senderId!!.uid, receiverId, message)
            }
            // when the user successfully to send message, the edit text will be empty
            messageSend_messageChat.text = null
        }

        // listener the image button to attach file message

        attach_file_messageChat.setOnClickListener {

            // sharing a file
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 438)
        }
    }
    // the function of sending message to user. auto generate message key, using hash map to push data
    private fun sendMessageToUser(senderId: String, receiverId: String?, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key
        val messageMap = HashMap<String,Any?>()
        messageMap["sender"] = senderId
        messageMap["message"] = message
        messageMap["receiver"] = receiverId
        messageMap["issue"] = false
        messageMap["url"] = ""
        messageMap["messageId"] = messageKey

        // create a new table named "chats",
        reference.child("Chats")
            .child(messageKey!!)
            .setValue(messageMap)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    // create other table named chatList by sender id
                    val chatListReference = FirebaseDatabase.getInstance().reference
                        .child("ChatList")
                        .child(senderId)
                        .child(receiverId!!)
                    chatListReference.addListenerForSingleValueEvent(object  : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if(!p0.exists()){
                                chatListReference.child("id").setValue(receiverId)
                            }

                            //// upload data by receiver id depending of receiver id
                            val chatListReferenceRef = FirebaseDatabase.getInstance().reference
                                .child("ChatList")
                                .child(receiverId)
                                .child(senderId!!)
                            chatListReferenceRef.child("id").setValue(senderId)
                        }

                    })

                    val reference = FirebaseDatabase.getInstance().reference
                        .child("Users").child(senderId)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 438 && resultCode == RESULT_OK && data!= null && data!!.data !=null){
            val loadingBar = ProgressDialog(this)
            loadingBar.setMessage("Message sending...")
            loadingBar.show()

            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Image")
            val ref = FirebaseDatabase.getInstance().reference
            val messagePushId = ref.push().key
            val filePath = storageReference.child("$messagePushId.jpg")

            var uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)
            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                if(!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }

                }

                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val messageMap = HashMap<String,Any?>()
                    messageMap["sender"] = senderId!!.uid
                    messageMap["message"] = "send you an image"
                    messageMap["receiver"] = receiverId
                    messageMap["issue"] = false
                    messageMap["url"] = url
                    messageMap["messageId"] = messagePushId
                    ref.child("Chats").child(messagePushId!!).setValue(messageMap)
                    loadingBar.dismiss()
                }
            }

        }
    }

    private fun retrieveMessage(senderId: String, receiverId: String?, imageUrl: String?) {
        mChatList = ArrayList()
        val ref = FirebaseDatabase.getInstance().reference.child("Chats")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mChatList as ArrayList<Chat>).clear()
                for (snapshot in p0.children){
                    val chat = snapshot.getValue(Chat::class.java)

                    if(chat!!.receiver.equals(senderId) && chat.sender.equals(receiverId)
                        || chat.receiver.equals(receiverId) && chat.sender.equals((senderId))){

                        (mChatList as ArrayList<Chat>).add(chat)
                    }
                    chatAdapter = ChatAdapter(this@MessageChatActivity,  (mChatList as ArrayList<Chat>),imageUrl!!)
                    recycler_view_chats.adapter = chatAdapter
                }
            }

        })


    }


}

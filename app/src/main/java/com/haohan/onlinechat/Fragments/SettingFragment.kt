package com.haohan.onlinechat.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EdgeEffect
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

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
    private val reqCode = 438
    private var imageUri: Uri? = null
    private var storageReference: StorageReference? = null
    private var coverChecker: String? = null
    private var socialChecker: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       val view = inflater.inflate(R.layout.fragment_setting, container, false)
        mAuth = FirebaseAuth.getInstance().currentUser
        databaseRef = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth!!.uid)
        storageReference = FirebaseStorage.getInstance().reference.child("User Images")

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

        view.setting_circleView.setOnClickListener{
            pickImage()
        }
        view.cover_image.setOnClickListener {
            coverChecker = "cover"
            pickImage()
        }

        view.set_facebook.setOnClickListener {
            socialChecker = "facebook"
            setSocialLinks()
        }

        view.set_instargram.setOnClickListener {
            socialChecker = "instagram"
            setSocialLinks()
        }

        view.set_website.setOnClickListener {
            socialChecker = "website"
            setSocialLinks()
        }

        return view
    }

    private fun setSocialLinks() {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(requireContext(),R.style.Theme_AppCompat_DayNight_Dialog_Alert)

        if(socialChecker == "website"){
            builder.setTitle("Write url: ")

        }else{
            builder.setTitle("Write user name")
        }

        val editText = EditText(context)
        if(socialChecker == "website"){
            editText.hint= "e.g www.google.com"

        }else{
            editText.hint = "Hao han"
        }

        builder.setView(editText)
        builder.setPositiveButton("Create", DialogInterface.OnClickListener { dialogInterface, i ->
            val str = editText.text.toString()
            if(str == ""){
                Toast.makeText(context,"Please write your website", Toast.LENGTH_SHORT).show()
            }else{
                updateSocialLink(str)
            }
        })
       builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.cancel()
       })
        builder.show()
    }

    private fun updateSocialLink(str: String) {

        val mapSocial = HashMap<String,Any>()

        when(socialChecker){
            "facebook" ->{
                mapSocial["facebook"] = "https://m.facebook.com/$str"
            }
            "instagram" ->{
                mapSocial["instagram"] = "https://m.instargram.com/$str"
            }
            "website" ->{
                mapSocial["website"] = "https://$str"
            }
        }
        databaseRef!!.updateChildren(mapSocial).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(context,"upload your social website was successful...",Toast.LENGTH_SHORT).show()

        } }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,reqCode)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == reqCode && resultCode == Activity.RESULT_OK && data!!.data!= null ){
                imageUri = data.data
                Toast.makeText(context,"uploading....",Toast.LENGTH_SHORT).show()
            uploadImage()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun uploadImage() {
        val progressBar =  ProgressDialog(context)
        progressBar.setMessage("image is uploading....")
        progressBar.setTitle("image")
        progressBar.show()

//        var builder = AlertDialog.Builder(context)
//        builder.setCancelable(false)
//        builder.setView(R.layout.fragment_setting)
//        var dialog = builder.create()
//        dialog.show()


        if(imageUri != null){
            val fileRef = storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)
            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot,Task<Uri>>{ task ->
                if(!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }

                }

                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener() { task->
                if(task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    if(coverChecker == "cover"){
                        val mapCoverImg = HashMap<String, Any>()
                        mapCoverImg["cover"] = url
                        databaseRef!!.updateChildren(mapCoverImg)
                        coverChecker = ""
                    }else{
                        val mapProfileImg = HashMap<String, Any>()
                        mapProfileImg["profile"] = url
                        databaseRef!!.updateChildren(mapProfileImg)
                        coverChecker = ""
                    }
                    progressBar.dismiss()
                   // dialog.dismiss()
                }
            }
        }
    }

}

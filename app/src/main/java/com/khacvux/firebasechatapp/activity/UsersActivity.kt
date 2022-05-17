package com.khacvux.firebasechatapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codingwithme.firebasechat.adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.khacvux.firebasechatapp.R
//import com.khacvux.firebasechatapp.adapter.UserAdapter
import com.khacvux.firebasechatapp.model.User
import kotlinx.android.synthetic.main.activity_users.*


class UsersActivity : AppCompatActivity() {

    var userList = ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)


        usersRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        imgBack.setOnClickListener {
            onBackPressed()
        }

        imgProfile.setOnClickListener {
            val intent = Intent(
                this@UsersActivity,
                ProfileActivity::class.java
            )
            startActivity(intent)
        }
        getUsersList()
    }
    fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference:DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)

                if (currentUser!!.profileImage == ""){
                    imgProfile.setImageResource(R.drawable.default_avatar)
                }else{
                    Glide.with(this@UsersActivity).load(currentUser.profileImage).into(imgProfile)
                }

                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(User::class.java)

                    if(!user!!.userId.equals(firebase.uid)){
                        userList.add(user)
                    }
                }
                val userAdapter = UserAdapter(this@UsersActivity, userList)

                usersRecyclerView.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

}
package com.khacvux.firebasechatapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.khacvux.firebasechatapp.R
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val userName = entName.text.toString()
            val email = entEmail.text.toString()
            val password = entPassword.text.toString()
            val confirmPassword = entConfirmPassword.text.toString()

            if(TextUtils.isEmpty(userName)){
                Toast.makeText(applicationContext, "Username is not required", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext, "Email is not required", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext, "Password is not required", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(applicationContext, "Confirm password is not required", Toast.LENGTH_SHORT).show()
            }
            else if(password != confirmPassword){
                Toast.makeText(applicationContext, "password not match", Toast.LENGTH_SHORT).show()
            }
            else{
                registerUser(userName, email, password)
            }
        }

        btnLogin.setOnClickListener{
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }




    }
    private fun registerUser(userName: String, email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {

                if(it.isSuccessful){
                    Toast.makeText(applicationContext, "it: true", Toast.LENGTH_SHORT).show()

                    val user: FirebaseUser? = auth.currentUser
                    val userId: String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String, String> = HashMap()
                    hashMap.put("userId", userId)
                    hashMap.put("userName", userName)
                    hashMap.put("email", email)
                    hashMap.put("profileImage", "")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if(it.isSuccessful){
                            //navigate to home screen
                            val intent = Intent(this@SignUpActivity,
                                UsersActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

    }

}
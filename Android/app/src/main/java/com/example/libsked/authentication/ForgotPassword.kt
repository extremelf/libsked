package com.example.libsked.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.libsked.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class ForgotPassword : AppCompatActivity() {

    lateinit var email: EditText
    private lateinit var btnReset: Button
    private lateinit var btnLogin: Button
    lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Ligar às Views do XML
        email = findViewById(R.id.et_email)
        btnReset = findViewById(R.id.bt_continue)
        btnLogin = findViewById(R.id.bt_signup)
        backButton = findViewById(R.id.back_button)

        // Back Button
        backButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnReset.setOnClickListener {

            val email1 = email.text.toString()

            if (email1. isEmpty()){
                email.error = "Email Can´t Be Blank!"
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email1)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Email Sent Successfully To Reset Your Password! ", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

        btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}
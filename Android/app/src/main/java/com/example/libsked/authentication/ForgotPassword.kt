package com.example.libsked.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.libsked.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class ForgotPassword : AppCompatActivity() {

    lateinit var Email: EditText
    private lateinit var btnReset: Button
    private lateinit var btnLogin: Button
    lateinit var BackButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Ligar às Views do XML
        Email = findViewById(R.id.et_email)
        btnReset = findViewById(R.id.bt_continue)
        btnLogin = findViewById(R.id.bt_signup)
        BackButton = findViewById(R.id.back_button)

        // Back Button
        BackButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnReset.setOnClickListener {

            val email = Email.text.toString()

            if (email. isEmpty()){
                Toast.makeText(this, "Please Enter Email Address!", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
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
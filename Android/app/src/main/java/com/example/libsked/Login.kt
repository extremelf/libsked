package com.example.libsked


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var Email: EditText
    private lateinit var Pass: EditText
    lateinit var btnLogin: Button
    private lateinit var RedirectRegister: TextView

    // Criar FireBaseAuth
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Ligar às Views do XML
        Email = findViewById(R.id.et_email)
        Pass = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        RedirectRegister = findViewById(R.id.tv_create_account)

        // Inicializar Auth
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            login()
        }

       RedirectRegister.setOnClickListener {
            val intent = Intent(this, register::class.java)
            startActivity(intent)
            // finish() para acabar a atividade
            finish()
        }
    }

    private fun login() {
        val email = Email.text.toString()
        val pass = Pass.text.toString()

        // Chamar a função signInWithEmailAndPassword(email, pass)
        // usando o auth do Firebase
        // Se for com sucesso, mostra um Toast

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Logged In!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // finish() para acabar a atividade
                finish()
            } else
                Toast.makeText(this, "Log In failed! ", Toast.LENGTH_SHORT).show()
        }
    }
}
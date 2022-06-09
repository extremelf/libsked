package com.example.libsked.authentication


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.libsked.MainActivity
import com.example.libsked.R
import com.google.firebase.auth.FirebaseAuth

const val SHARED_PREF_NAME = "USERINFO"

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
        val sharedPref: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        val uid = sharedPref.getString("USERID", "")

        if (uid != null) {
            if(uid.isNotBlank()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
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
                val id = auth.currentUser?.uid
                if (id != null) {
                    saveToSharedPref(id)
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // finish() para acabar a atividade
                finish()
            } else
                Toast.makeText(this, "Log In failed! ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToSharedPref(uid: String){
        val sharePref: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        sharePref
            .edit()
            .putString("USERID", uid)
            .apply()

    }
}
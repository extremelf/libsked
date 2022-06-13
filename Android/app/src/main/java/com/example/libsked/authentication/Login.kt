package com.example.libsked.authentication


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.libsked.MainActivity
import com.example.libsked.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

const val SHARED_PREF_NAME = "USERINFO"

class Login : AppCompatActivity() {

    lateinit var email: EditText
    private lateinit var pass: EditText
    lateinit var btnLogin: Button
    private lateinit var redirectRegister: TextView
    lateinit var forgotPass: TextView
    lateinit var progressBar: ProgressBar

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
        email = findViewById(R.id.et_email)
        pass = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        redirectRegister = findViewById(R.id.tv_create_account)
        forgotPass = findViewById(R.id.tv_forgot_password)
        progressBar = findViewById(R.id.progressBar)

        // Inicializar Auth
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            login()
        }

       redirectRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            // finish() para acabar a atividade
            finish()
       }

        forgotPass.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val email1 = email.text.toString()
        val pass1 = pass.text.toString()

        progressBar.visibility = View.VISIBLE

        // Chamar a função signInWithEmailAndPassword(email, pass)
        // usando o auth do Firebase
        // Se for com sucesso, mostra um Toast

        if(email1.isNotEmpty() && pass1.isNotEmpty()){
            auth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully Logged In!", Toast.LENGTH_SHORT).show()
                    val id = auth.currentUser?.uid
                    if (id != null) {
                        saveToSharedPref(id)
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    progressBar.visibility = View.GONE
                    // finish() para acabar a atividade
                    finish()
                } else
                    Toast.makeText(this, "Log In failed! ", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        } else {
            progressBar.visibility = View.GONE
            email.error = "Invalid Email"
            pass.error = "Invalid Password"
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
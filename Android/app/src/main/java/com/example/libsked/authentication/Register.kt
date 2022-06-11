package com.example.libsked.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.libsked.R
import com.example.libsked.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    private lateinit var database : DatabaseReference

    lateinit var Email: EditText
    lateinit var Nome: EditText
    lateinit var NumeroAluno: EditText
    private lateinit var Pass: EditText
    lateinit var ConfPass: EditText
    private lateinit var btnRegister: Button
    //lateinit var RedirectLogin: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Ligar às Views do XML
        Email = findViewById(R.id.et_email)
        Nome = findViewById(R.id.et_nome)
        NumeroAluno = findViewById(R.id.et_number)
        Pass = findViewById(R.id.et_password)
        ConfPass = findViewById(R.id.et_passwordRepeat)
        btnRegister = findViewById(R.id.btn_register)
        //RedirectLogin = findViewById(R.id.GoToLogin)

        // Inicializar Auth
        auth = Firebase.auth

        btnRegister.setOnClickListener {
            registerUser()
        }

        // Mudar de Página Registo para Página Login
        /* RedirectLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        } */

    }

    private fun registerUser() {

        val email = Email.text.toString()
        val name = Nome.text.toString()
        val number = NumeroAluno.text.toString()
        val pass = Pass.text.toString()
        val confirmPassword = ConfPass.text.toString()

        // Verificar se existem campos não preenchidos
        if (email.isBlank() || name.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Fields can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar se as Passwords Coincidem
        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        progressBar.visibility = View.VISIBLE

        // Se tudo estiver correto
        // Chamamos createUserWithEmailAndPassword
        // Usando Auth passamos o email e a password
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {

                database = FirebaseDatabase.getInstance().getReference("users")
                val user = Users(name, email, number, pass)
                database.child(email).setValue(user)

                Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                // finish() para acabar a atividade
                finish()
                progressBar.visibility = View.GONE
            } else {
                Toast.makeText(this, "Register Failed!", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        }
    }
}
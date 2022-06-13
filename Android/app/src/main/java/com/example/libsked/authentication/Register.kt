package com.example.libsked.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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

    lateinit var email: EditText
    lateinit var nome: EditText
    lateinit var numeroAluno: EditText
    private lateinit var pass: EditText
    lateinit var confPass: EditText
    private lateinit var btnRegister: Button
    lateinit var backButton: ImageButton

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Ligar às Views do XML
        email = findViewById(R.id.et_email)
        nome = findViewById(R.id.et_nome)
        numeroAluno = findViewById(R.id.et_number)
        pass = findViewById(R.id.et_password)
        confPass = findViewById(R.id.et_passwordRepeat)
        btnRegister = findViewById(R.id.btn_register)
        backButton = findViewById(R.id.back_button)

        // Inicializar Auth
        auth = Firebase.auth

        btnRegister.setOnClickListener {
            registerUser()
        }

        // Back Button
        backButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }

    private fun registerUser() {

        val email1 = email.text.toString()
        val name1 = nome.text.toString()
        val number1 = numeroAluno.text.toString()
        val pass1 = pass.text.toString()
        val confirmPassword1 = confPass.text.toString()

        // Verificar se existem campos não preenchidos
        if (email1.isBlank() && name1.isBlank() && pass1.isBlank() && confirmPassword1.isBlank()) {
            email.error = "Email Can´te Be Blank!"
            nome.error = "Name Can´t Be Blank!"
            pass.error = "Password Can´t Be Blank!"
            confPass.error = "Repeat Password Can´t Be Blank!"
            return
        }

        // Verificar se tem 6 caracteres
        if (pass1.length < 6){
            pass.error = "Password Must Be 6 Characters Minimum!"
            return
        }

        // Verificar se as Passwords Coincidem
        if (pass1 != confirmPassword1) {
            confPass.error = "Password and Repeat Password Must Coincide!"
            return
        }

        progressBar.visibility = View.VISIBLE

        // Se tudo estiver correto
        // Chamamos createUserWithEmailAndPassword
        // Usando Auth passamos o email e a password
        auth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val id = auth.currentUser?.uid
                database = FirebaseDatabase.getInstance().getReference("users")
                val user = Users(name1, email1, number1)
                if (id != null) {
                    database.child(id).setValue(user)
                }

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
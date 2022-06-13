package com.example.libsked.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.libsked.R
import com.example.libsked.authentication.Login
import com.example.libsked.authentication.SHARED_PREF_NAME
import com.example.libsked.model.Users
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.fragment_account.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var database: DatabaseReference
    private lateinit var uid: String
    private var userInfo: Users? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        uid = sharedPref.getString("USERID", "").toString()

        database = FirebaseDatabase.getInstance().getReference("users")

        database.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userInfo = snapshot.getValue<Users>()
                username_account.text = userInfo?.name ?: "Username"
                et_email.text = resources.getString(R.string.email, userInfo?.email)
                et_name.text = resources.getString(R.string.name, userInfo?.name)
                et_number.text = resources.getString(R.string.number, userInfo?.number)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        logoutButton.setOnClickListener {
            logout()
        }

        edit_password.setOnClickListener {
            editPassword()
        }

        submit_password_change.setOnClickListener {
            changePassword()
        }
    }

    private fun editPassword() {
        if (editPasswordLayout.visibility == View.GONE) {
            editPasswordLayout.visibility = View.VISIBLE
        } else {
            editPasswordLayout.visibility = View.GONE
        }

    }

    private fun changePassword() {
        progressBar.visibility = View.VISIBLE
        val user = FirebaseAuth.getInstance().currentUser

        if(et_oldpass.text.isNotEmpty() && et_newpass.text.isNotEmpty() && et_newpass2.text.isNotEmpty() && et_newpass.text.toString() == et_newpass2.text.toString()){
            val credential = userInfo!!.email?.let {
                EmailAuthProvider.getCredential(
                    it,
                    et_oldpass.text.toString()
                )
            }

            credential?.let {
                user?.reauthenticate(it)
                    ?.addOnCompleteListener(OnCompleteListener{
                        if(it.isSuccessful){
                            user.updatePassword(et_newpass.text.toString()).addOnCompleteListener(OnCompleteListener{
                                if(it.isSuccessful){
                                    Toast.makeText(requireContext(), "Password change successful", Toast.LENGTH_SHORT).show()
                                    et_oldpass.setText("")
                                    et_newpass.setText("")
                                    et_newpass2.setText("")
                                    editPasswordLayout.visibility = View.GONE
                                    progressBar.visibility = View.GONE
                                } else {
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(requireContext(), "Password change failed", Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Invalid old password provided", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        } else {
            progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), "Invalid data provided", Toast.LENGTH_SHORT).show()
        }


    }

    private fun logout() {
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPref
            .edit()
            .remove("USERID")
            .apply()
        val intent = Intent(requireActivity(), Login::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.example.libsked

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.libsked.fragments.AccountFragment
import com.example.libsked.fragments.AppointmentFragment
import com.example.libsked.fragments.MainFragment
import com.example.libsked.fragments.QrCodeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainFragment = MainFragment()
    private val appointmentFragment = AppointmentFragment()
    private val qrCodeFragment = QrCodeFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(mainFragment)

        bottomNavigation.setOnItemReselectedListener {
            when(it.itemId){
                R.id.account -> replaceFragment(accountFragment)
                R.id.appointments -> replaceFragment(appointmentFragment)
                R.id.home -> replaceFragment(mainFragment)
                R.id.qr_code -> replaceFragment(qrCodeFragment)
            }
        }
    }



    private fun replaceFragment(fragment: Fragment){

        if(fragment!=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, fragment)
            transaction.commit()
        }
    }
}
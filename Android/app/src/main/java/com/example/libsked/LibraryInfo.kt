package com.example.libsked

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageSwitcher


class LibraryInfo : AppCompatActivity() {

    lateinit var btnPrevious : ImageButton
    lateinit var btnNext     : ImageButton
    lateinit var imageSwitcher : ImageSwitcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library_info)


    }
}
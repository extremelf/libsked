package com.example.libsked

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.libsked.fragments.TimePickerFragment

class RoomSchedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_schedule)


    }

    fun showTimePickerDialog(view: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }


}
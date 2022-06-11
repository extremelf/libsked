package com.example.libsked.adapters

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.libsked.R
import com.example.libsked.R.color.occupied_red
import com.example.libsked.fragments.ScheduleInfo
import com.example.libsked.model.Schedule
import kotlinx.android.synthetic.main.schedule_line.view.*
import java.sql.Timestamp
import java.util.*

class ScheduleLineAdapater :
    RecyclerView.Adapter<ScheduleViewHolder>() {
    private lateinit var scheduleList: List<ScheduleInfo>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.schedule_line,
                    parent,
                    false
                )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
       if(this::scheduleList.isInitialized){
           val currentHour = scheduleList[position]
           holder.apply {
               if(currentHour.minute == 0){
                   hours.text = currentHour.hour.toString()
               } else {
                   hours.text = ""
               }
               if(!currentHour.isOccupied){
                   hours.setBackgroundResource(R.drawable.rectangle_green)
               } else {
                   hours.setBackgroundResource(R.drawable.rectangle_red)
               }
           }
       }
    }

    override fun getItemCount(): Int {
        return if(this::scheduleList.isInitialized){
            scheduleList.size
        } else{
            0
        }

    }

    fun changeList(scheduleList: List<ScheduleInfo>){
        this.scheduleList = scheduleList
        notifyDataSetChanged()
    }
}

class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val hours = itemView.tvHours
}
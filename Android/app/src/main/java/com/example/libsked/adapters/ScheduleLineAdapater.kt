package com.example.libsked.adapters

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.libsked.R
import com.example.libsked.R.color.occupied_red
import com.example.libsked.model.Schedule
import kotlinx.android.synthetic.main.schedule_line.view.*
import java.sql.Timestamp
import java.util.*

class ScheduleLineAdapater :
    RecyclerView.Adapter<ScheduleViewHolder>() {
    private lateinit var scheduleList: List<Schedule>
    private val hours = (0..23).toList()
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
        var isClear = true
        val currentHour = hours[position]
        if(this::scheduleList.isInitialized) {
            for (schedule in scheduleList) {
                val scheduleStart = Timestamp(schedule.start)
                val currentHourTimestamp = Timestamp(Calendar.getInstance().timeInMillis)
                currentHourTimestamp.hours = currentHour
                if (scheduleStart.hours == currentHourTimestamp.hours) {
                    isClear = false
                }
            }
        }

        holder.apply {
            hours.text = currentHour.toString()
            if(isClear){
                hours.setBackgroundResource(R.drawable.rectangle_green)
            } else {
                hours.setBackgroundResource(R.drawable.rectangle_red)
            }
        }
    }

    override fun getItemCount(): Int {
        return hours.size
    }

    fun changeList(scheduleList: List<Schedule>){
        this.scheduleList = scheduleList
        notifyDataSetChanged()
    }
}

class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val hours = itemView.tvHours
}
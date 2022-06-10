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
    private val hoursList = (7..32).toList()
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
        val currentHour = hoursList[position]
        if(this::scheduleList.isInitialized) {
            for (schedule in scheduleList) {
                val scheduleStart = Timestamp(schedule.start)
                val scheduleEnd = Timestamp(schedule.end)
                val currentHourTimestamp = Timestamp(Calendar.getInstance().timeInMillis)

                if(currentHour % 2 != 0){
                    currentHourTimestamp.hours = (hoursList[0] + ((currentHour - hoursList[0]) / 2))
                    currentHourTimestamp.minutes = 0

                } else {
                    currentHourTimestamp.hours = (hoursList[0] + ((currentHour-1 - hoursList[0]) / 2))
                    currentHourTimestamp.minutes = 30
                }
                if (scheduleStart <= currentHourTimestamp && scheduleEnd > currentHourTimestamp) {
                    isClear = false
                }
            }
        }

        holder.apply {
            if(currentHour%2 != 0){
                hours.text = (hoursList[0] + ((currentHour - hoursList[0]) / 2)).toString()
            } else {
                hours.text = ""
            }
            if(isClear){
                hours.setBackgroundResource(R.drawable.rectangle_green)
            } else {
                hours.setBackgroundResource(R.drawable.rectangle_red)
            }
        }
    }

    override fun getItemCount(): Int {
        return hoursList.size
    }

    fun changeList(scheduleList: List<Schedule>){
        this.scheduleList = scheduleList
        notifyDataSetChanged()
    }
}

class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val hours = itemView.tvHours
}
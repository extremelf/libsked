package com.example.libsked.adapters

import android.R.attr.data
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.libsked.R
import com.example.libsked.model.Schedule
import kotlinx.android.synthetic.main.history_line.view.*
import kotlinx.android.synthetic.main.schedule_line.view.*
import java.sql.Timestamp
import java.util.*


class HistoryLineAdapter : RecyclerView.Adapter<HistoryViewHolder>() {
    private lateinit var scheduleList: MutableList<Schedule>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.history_line,
                    parent,
                    false
                )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        if (this::scheduleList.isInitialized) {
            val currentSchedule = scheduleList[position]
            var isAfter = false
            for (schedule in scheduleList) {
                val currentHourTimestamp = Timestamp(Calendar.getInstance().timeInMillis)
                if (currentSchedule.end > currentHourTimestamp.time) {
                    isAfter = true
                }

            }
            holder.apply {
                if (isAfter) {
                    flag.text = "schedule completed"
                } else {
                    flag.text = "Schedule booked"
                }
                roomID.text ="Room: " + scheduleList[position].roomId.toString()
                val auxDuration = (scheduleList[position].end - scheduleList[position].start)/3.6e+6


                duration.text = "Reservation duration: " + auxDuration.toString() + "Hours"
                startTime.text ="Start: "+ Timestamp(scheduleList[position].start).hours.toString()
                endTime.text = "End: " + Timestamp(scheduleList[position].end).hours.toString()
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

    fun changeList(scheduleList: List<Schedule>) {
        this.scheduleList = scheduleList.toMutableList()
        notifyDataSetChanged()
    }



    fun clear(){
        val size: Int = scheduleList.size
        scheduleList.clear()
        notifyItemRangeRemoved(0, size)
    }
}

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val roomID = itemView.roomID
    val duration = itemView.duration
    val startTime = itemView.startTime
    val endTime = itemView.endTime
    val flag = itemView.flag
}

class TimeInHours(val hours: Int, val minutes: Int, val seconds: Int) {
    override fun toString(): String {
        return String.format("%dh : %02dm : %02ds", hours, minutes, seconds)
    }


}
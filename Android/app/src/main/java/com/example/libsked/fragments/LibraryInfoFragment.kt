package com.example.libsked.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libsked.R
import com.example.libsked.adapters.ScheduleLineAdapater
import com.example.libsked.appplication.ScheduleApplication
import com.example.libsked.model.*
import kotlinx.android.synthetic.main.activity_library_info.*
import kotlinx.android.synthetic.main.activity_room_schedule.*
import java.sql.Timestamp
import java.util.*

class LibraryInfoFragment: Fragment() {

    private var roomNumber: Int? = null
    private lateinit var availablehours: MutableList<ScheduleInfo>

    private val scheduleViewModel: ScheduleViewModel by activityViewModels{
        ScheduleViewModelFactory((requireActivity().application as ScheduleApplication).repository)
    }
    private val roomViewModel: RoomViewModel by activityViewModels {
        RoomViewModelFactory((requireActivity().application as ScheduleApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        arguments?.let{
            roomNumber = it.getInt("RoomNumber")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_library_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val scheduleAdapter = ScheduleLineAdapater()
        schedule_recycler_library.apply{
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }

        roomNumber?.let {
            scheduleViewModel.getDayScheduleOfLibrary().observe(viewLifecycleOwner, Observer{
                populateScheduleInfo(it)
                scheduleAdapter.changeList(availablehours)
            })
            roomViewModel.getRoomInfo(it).observe(viewLifecycleOwner, Observer { item ->
                populateInfo(item)
            })
        }
    }

    private fun populateScheduleInfo(scheduleList: List<Schedule>) {
        val hoursOfDay = (7..32).toList()
        val hours: MutableList<ScheduleInfo> = mutableListOf()
        for (position in hoursOfDay) {
            val hour = (hoursOfDay[0] + ((position - hoursOfDay[0]) / 2))
            var minute: Int
            var isOccupied = false
            if (position % 2 == 0) {
                minute = 30
            } else {
                minute = 0
            }
            for (schedule in scheduleList) {
                val scheduleStart = Timestamp(schedule.start)
                val scheduleEnd = Timestamp(schedule.end)
                val currentHourTimestamp = Timestamp(Calendar.getInstance().timeInMillis)
                currentHourTimestamp.hours = hour
                currentHourTimestamp.minutes = minute

                if (scheduleStart <= currentHourTimestamp && scheduleEnd > currentHourTimestamp) {
                    isOccupied = true
                }
            }
            val hourInfoInit = ScheduleInfo(hour = hour, minute = minute, isOccupied = isOccupied)
            hours.add(hourInfoInit)
        }
        availablehours = hours
    }

    private fun populateInfo(room: RoomTable) {
        room_number_library.text = requireActivity().resources.getString(R.string.library)
        ("Tables: " + room.tablesNumber).also { room_tables_library.text = it }
        ("Chairs: " + room.chairsNumber).also { room_chairs_library.text = it }
        ("Sockets: " + room.socketsNumber).also { room_sockets_library.text = it }
    }
}
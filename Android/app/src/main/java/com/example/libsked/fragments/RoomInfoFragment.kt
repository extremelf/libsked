package com.example.libsked.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libsked.R
import com.example.libsked.adapters.ScheduleLineAdapater
import com.example.libsked.appplication.ScheduleApplication
import com.example.libsked.model.*
import kotlinx.android.synthetic.main.activity_room_schedule.*

class RoomInfoFragment: Fragment() {
    private var roomNumber: Int? = null
    private lateinit var scheduleAdapter: ScheduleLineAdapater
    private val roomViewModel: RoomViewModel by activityViewModels{
        RoomViewModelFactory((requireActivity().application as ScheduleApplication).repository)
    }
    private val scheduleViewModel: ScheduleViewModel by activityViewModels{
        ScheduleViewModelFactory((requireActivity().application as ScheduleApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        arguments?.let{
            roomNumber = it.getInt("RoomNumber")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        return inflater.inflate(R.layout.activity_room_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        ("Room: $roomNumber").also { room_number.text = it }
        scheduleAdapter = ScheduleLineAdapater()
        schedule_recycler.apply{
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }


        roomNumber?.let { scheduleViewModel.getRoomSchedule(it).observe(viewLifecycleOwner, Observer { item ->
            scheduleAdapter.changeList(item)
        }) }
        roomNumber?.let {
            roomViewModel.getRoomInfo(it).observe(viewLifecycleOwner, Observer { item ->
                populateInfo(item)
            })
        }

    }

    private fun populateInfo(room: RoomTable){
        ("Tables: " + room.tablesNumber).also { room_tables.text = it }
        ("Chairs: " + room.chairsNumber).also { room_chairs.text = it }
        ("Sockets: " + room.socketsNumber).also { room_sockets.text = it }

    }

    companion object{
        @JvmStatic
        fun newInstance(roomNumber: Int) =
            MainFragment().apply {
                arguments = Bundle().apply{
                    putInt("RoomNumber", roomNumber)
                }
            }
    }
}
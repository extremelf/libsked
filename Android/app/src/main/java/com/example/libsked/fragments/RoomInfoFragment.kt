package com.example.libsked.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libsked.R
import com.example.libsked.adapters.ScheduleLineAdapater
import com.example.libsked.appplication.ScheduleApplication
import com.example.libsked.authentication.SHARED_PREF_NAME
import com.example.libsked.model.*
import kotlinx.android.synthetic.main.activity_room_schedule.*
import java.sql.Timestamp
import java.util.*

class RoomInfoFragment : Fragment() {
    private var roomNumber: Int? = null
    private lateinit var scheduleAdapter: ScheduleLineAdapater
    private lateinit var availablehours: MutableList<ScheduleInfo>
    private lateinit var sharedPref: SharedPreferences
    private val roomViewModel: RoomViewModel by activityViewModels {
        RoomViewModelFactory((requireActivity().application as ScheduleApplication).repository)
    }
    private val scheduleViewModel: ScheduleViewModel by activityViewModels {
        ScheduleViewModelFactory((requireActivity().application as ScheduleApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            roomNumber = it.getInt("RoomNumber")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_room_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref =
            requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val uid = sharedPref.getString("USERID", "")



        ("Room: $roomNumber").also { room_number.text = it }
        scheduleAdapter = ScheduleLineAdapater()
        schedule_recycler.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }

        roomNumber?.let {
            scheduleViewModel.getRoomScheduleOfDay(it)
                .observe(viewLifecycleOwner, Observer { item ->
                    populateScheduleInfo(item)
                    scheduleAdapter.changeList(availablehours)
                    spinnerStart.adapter = spinnerAdapter(availablehours)
                    uid?.let { uid ->
                        scheduleViewModel.getActiveReservations(uid, Calendar.getInstance().timeInMillis)
                            .observe(viewLifecycleOwner, Observer {
                                if (it.isNotEmpty()) {
                                    val canExtend = !hasReservationAfter(it[0])
                                    val currentTimeStamp = Timestamp(Calendar.getInstance().timeInMillis)
                                    if ((it[0].end - currentTimeStamp.time) <= 1800000 && canExtend) {
                                        reservationTitle.text = resources.getText(R.string.extend_reservation)
                                        reservationButton.setOnClickListener { view ->
                                            extendReservationListener(it[0])
                                        }
                                        reservationButton.visibility = View.VISIBLE
                                        reservationButton.isEnabled = true
                                    } else {
                                        reservationTitle.text = resources.getText(R.string.active_reservation)
                                        reservationButton.isEnabled = false
                                        reservationButton.visibility = View.GONE
                                    }
                                    startTv.visibility = View.GONE
                                    startTv.isEnabled = false
                                    endTv.visibility = View.GONE
                                    endTv.isEnabled = false
                                    spinnerStart.visibility = View.GONE
                                    spinnerStart.isEnabled = false
                                    spinnerEnd.visibility = View.GONE
                                    spinnerEnd.isEnabled = false

                                } else {
                                    reservationTitle.text = resources.getText(R.string.make_a_reservation)
                                    spinnerStart.isEnabled = true
                                    spinnerEnd.isEnabled = true
                                    reservationButton.isEnabled = true

                                    spinnerStart.visibility = View.VISIBLE
                                    spinnerEnd.visibility = View.VISIBLE
                                    startTv.visibility = View.VISIBLE
                                    endTv.visibility = View.VISIBLE
                                }
                            })

                        scheduleViewModel.getNotConsumedReservations(uid).observe(viewLifecycleOwner, Observer {
                            if (it >= 1) {
                                reservationTitle.text = resources.getText(R.string.multiple_reservation)
                                spinnerStart.isEnabled = false
                                spinnerEnd.isEnabled = false
                                reservationButton.isEnabled = false
                            } else {
                                spinnerStart.isEnabled = true
                                spinnerEnd.isEnabled = true
                                reservationButton.isEnabled = true
                            }
                        })
                    }
                })
        }

        reservationButton.setOnClickListener(this::defaultReservationListener)

        roomNumber?.let {
            roomViewModel.getRoomInfo(it).observe(viewLifecycleOwner, Observer { item ->
                populateInfo(item)
            })
        }





        spinnerStart.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val hours: MutableList<ScheduleInfo> = mutableListOf()
                var occurrence = 0
                var initialHour = 0
                for (hour in 0 until availablehours.size) {
                    val separatedInfo = spinnerStart.selectedItem.toString().split(":")
                    if (availablehours[hour].hour == separatedInfo[0].toInt() && availablehours[hour].minute == separatedInfo[1].toInt()) {
                        initialHour = hour
                    }
                }
                for (item in initialHour + 1 until availablehours.size) {
                    if (!availablehours[item].isOccupied && occurrence < 1) {
                        hours.add(
                            ScheduleInfo(
                                hour = availablehours[item].hour,
                                minute = availablehours[item].minute,
                                isOccupied = availablehours[item].isOccupied
                            )
                        )
                    } else if (availablehours[item].isOccupied && occurrence < 1) {
                        hours.add(
                            ScheduleInfo(
                                hour = availablehours[item].hour,
                                minute = availablehours[item].minute,
                                isOccupied = availablehours[item].isOccupied
                            )
                        )
                        occurrence += 1
                    } else {
                        break
                    }
                }
                spinnerEnd.adapter = spinnerAdapter(hours, false)
                // parent.getItemAtPosition(pos)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }

    private fun extendReservationListener(activeSchedule: Schedule) {
        scheduleViewModel.updateEndOfReservation(activeSchedule.end + 1800000, activeSchedule.id)

    }

    private fun defaultReservationListener(view: View) {
        val uid = sharedPref.getString("USERID", "")

        val startTimestamp = Calendar.getInstance()
        val endTimestamp = Calendar.getInstance()
        val spinnerEndTime = spinnerEnd.selectedItem.toString().split(":")
        val spinnerStartTime = spinnerStart.selectedItem.toString().split(":")

        endTimestamp.set(Calendar.HOUR_OF_DAY,spinnerEndTime[0].toInt())
        endTimestamp.set(Calendar.MINUTE,spinnerEndTime[1].toInt())
        endTimestamp.set(Calendar.SECOND, 0)

        startTimestamp.set(Calendar.HOUR_OF_DAY, spinnerStartTime[0].toInt())
        startTimestamp.set(Calendar.MINUTE, spinnerStartTime[1].toInt())
        startTimestamp.set(Calendar.SECOND, 0)


        roomNumber?.let { it2 ->
            uid?.let {
                Schedule(
                    creation_timestamp = Calendar.getInstance().timeInMillis,
                    personId = it,
                    roomId = it2,
                    start = startTimestamp.timeInMillis,
                    end = endTimestamp.timeInMillis
                )
            }
        }?.let { it2 -> scheduleViewModel.insert(it2) }
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

    private fun hasReservationAfter(activeReservation: Schedule): Boolean {
        val activeReservationTimestamp = Timestamp(activeReservation.end)
        for (hourPos in 0 until availablehours.size) {
            if (availablehours[hourPos].hour == activeReservationTimestamp.hours && availablehours[hourPos].minute == activeReservationTimestamp.minutes) {
                if ((hourPos + 1) < availablehours.size) {
                    return availablehours[hourPos + 1].isOccupied
                }
            }
        }
        return false
    }

    private fun spinnerAdapter(
        values: MutableList<ScheduleInfo>,
        start: Boolean = true
    ): SpinnerAdapter {
        val availableStarts: MutableList<String> = mutableListOf()
        val currentTime = Timestamp(Calendar.getInstance().timeInMillis)

        for (itemPos in 0 until if (start) values.size - 1 else values.size) {
            if ((values[itemPos].hour >= currentTime.hours && !values[itemPos].isOccupied) || !start) {
                if ((itemPos + 1) < values.size) {
                    if (values[itemPos].hour == currentTime.hours && values[itemPos].minute < currentTime.minutes && currentTime.minutes < (if (values[itemPos + 1].minute == 0) 59 else values[itemPos + 1].minute) || !start) {
                        availableStarts.add("${values[itemPos].hour}:${if (values[itemPos].minute == 0) "00" else values[itemPos].minute}")
                    } else if (values[itemPos].hour != currentTime.hours) {
                        availableStarts.add("${values[itemPos].hour}:${if (values[itemPos].minute == 0) "00" else values[itemPos].minute}")
                    }
                } else {
                    availableStarts.add("${values[itemPos].hour}:${if (values[itemPos].minute == 0) "00" else values[itemPos].minute}")
                }
            }
        }
        return ArrayAdapter(requireContext(), R.layout.spinner_item, availableStarts)
    }

    private fun populateInfo(room: RoomTable) {
        ("Tables: " + room.tablesNumber).also { room_tables.text = it }
        ("Chairs: " + room.chairsNumber).also { room_chairs.text = it }
        ("Sockets: " + room.socketsNumber).also { room_sockets.text = it }
    }

    companion object {
        @JvmStatic
        fun newInstance(roomNumber: Int) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putInt("RoomNumber", roomNumber)
                }
            }
    }
}

data class ScheduleInfo(
    val hour: Int,
    val minute: Int,
    val isOccupied: Boolean = false
)
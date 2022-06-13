package com.example.libsked.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libsked.R
import com.example.libsked.adapters.HistoryLineAdapter
import com.example.libsked.appplication.ScheduleApplication
import com.example.libsked.model.ScheduleViewModel
import com.example.libsked.model.ScheduleViewModelFactory
import kotlinx.android.synthetic.main.fragment_appointment.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val SHARED_PREF_NAME = "USERINFO"

/**
 * A simple [Fragment] subclass.
 * Use the [AppointmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppointmentFragment : Fragment() {

    private lateinit var historyAdapter: HistoryLineAdapter
    private val scheduleViewModel: ScheduleViewModel by activityViewModels {
        ScheduleViewModelFactory((requireActivity().application as ScheduleApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_appointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(
            com.example.libsked.authentication.SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )

        val uid = sharedPref.getString("USERID", "")

        historyAdapter = HistoryLineAdapter()
        history_recycler.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }


        var startOfDay = Calendar.getInstance().time
        startOfDay.hours = 0


        var endOfDay = Calendar.getInstance().time
        endOfDay.hours = 23
        endOfDay.minutes = 59

        btn_apply.text = endOfDay.hours.toString()

        scheduleViewModel.getScheduleOnXDay(startOfDay.time, endOfDay.time, uid.toString())
            .observe(viewLifecycleOwner, Observer { item ->
                historyAdapter.changeList(item)
            })




        calendar.setOnDateChangeListener(
            OnDateChangeListener { view, year, month, dayOfMonth ->

                val date = (dayOfMonth.toString() + "-"
                        + (month + 1) + "-" + year)
                val auxDate = convertToMilis(date)

                startOfDay = Timestamp(auxDate)
                startOfDay.hours = 0

                endOfDay = Timestamp(auxDate)
                endOfDay.hours = 23
                endOfDay.minutes = 59

                btn_apply.text = endOfDay.hours.toString()

                historyAdapter.clear()

                scheduleViewModel.getScheduleOnXDay(startOfDay.time, endOfDay.time, uid.toString())
                    .observe(viewLifecycleOwner, Observer { item ->
                        historyAdapter.changeList(item)
                    })
            })

    }

    fun convertToMilis(date: String): Long {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = sdf.parse(date)


        return date.time
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppointmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppointmentFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
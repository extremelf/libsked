package com.example.libsked.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.libsked.MainActivity
import com.example.libsked.R
import com.example.libsked.appplication.ScheduleApplication
import com.example.libsked.model.ScheduleViewModel
import com.example.libsked.model.ScheduleViewModelFactory
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val scheduleViewModel: ScheduleViewModel by activityViewModels {
        ScheduleViewModelFactory((requireActivity().application as ScheduleApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleViewModel.getRooms()
            .observe(viewLifecycleOwner, Observer { item ->
                for (roomNumber in item) {
                    val viewId = "room${roomNumber}"
                    val viewResId =
                        resources.getIdentifier(
                            viewId,
                            "id",
                            requireActivity().packageName
                        )
                    val room: TextView = requireView().findViewById<TextView>(viewResId)
                    room.background.setColorFilter(
                        Color.GREEN,
                        PorterDuff.Mode.SRC_IN)
                    room.setOnClickListener {
                        changeToRoomFragment(roomNumber)
                    }
                    scheduleViewModel.getRoomSchedule(roomNumber)
                        .observe(viewLifecycleOwner, Observer { data ->
                            for (schedule in data) {
                                val isOccupied =
                                    schedule.start < Calendar.getInstance().timeInMillis && Calendar.getInstance().timeInMillis < schedule.end
                                if (isOccupied) {
                                    room.background.setColorFilter(
                                        Color.RED,
                                        PorterDuff.Mode.SRC_IN
                                    )
                                }
                            }
                        })
                }
            })
    }

    private fun changeToRoomFragment(roomNumber: Int){
        val fragment = RoomInfoFragment()
        val bundle = Bundle()
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        //data to be sent to the new fragment
        bundle.putInt("RoomNumber", roomNumber)
        fragment.arguments = bundle

        //fragment change
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
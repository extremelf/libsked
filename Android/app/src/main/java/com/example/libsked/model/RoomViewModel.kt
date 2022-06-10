package com.example.libsked.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.libsked.repository.ScheduleRepository

class RoomViewModel(private val repository: ScheduleRepository): ViewModel() {
    fun getRoomSchedule(id: Int): LiveData<List<Schedule>> = repository.getRoomSchedule(id).asLiveData()
    fun getRoomInfo(id: Int): LiveData<RoomTable> = repository.getRoom(id).asLiveData()
}

class RoomViewModelFactory(private val repository: ScheduleRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass:Class<T>): T {
        if(modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
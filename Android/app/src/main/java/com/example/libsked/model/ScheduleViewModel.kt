package com.example.libsked.model

import androidx.lifecycle.*
import com.example.libsked.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {
    val allSchedules: LiveData<List<Schedule>> = repository.allSchedules.asLiveData()

    fun getRoomSchedule(roomId: Int): LiveData<List<Schedule>> =
        repository.getRoomSchedule(roomId).asLiveData()

    fun getRooms(): LiveData<List<Int>> = repository.getRooms().asLiveData()

    fun getActiveReservations(personId: String, currentTime: Long): LiveData<List<Schedule>> =
        repository.getActiveReservations(personId, currentTime).asLiveData()

    fun getRoomScheduleOfDay(roomId: Int): LiveData<List<Schedule>> =
        repository.getRoomScheduleOfDay(roomId).asLiveData()

    fun getScheduleOnXDay(starOfday: Long,endOfDay: Long, userID: String): LiveData<List<Schedule>> = repository.getScheduleOnXDay(starOfday,endOfDay,userID).asLiveData()

    /*fun isRoomOccupied(roomId: Int): Boolean{
        return repository.isRoomOccupied(roomId)
    }*/

    fun insert(schedule: Schedule) = viewModelScope.launch {
        repository.insert(schedule)
    }
}

class ScheduleViewModelFactory(private val repository: ScheduleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
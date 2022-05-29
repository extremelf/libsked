package com.example.libsked.model

import androidx.lifecycle.*
import com.example.libsked.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {
    val allSchedules: LiveData<List<Schedule>> = repository.allSchedules.asLiveData()

    fun getRoomSchedule(roomId: Int): LiveData<List<Schedule>>{
        return repository.getRoomSchedule(roomId).asLiveData()
    }

    fun insert(schedule: Schedule) = viewModelScope.launch {
        repository.insert(schedule)
    }
}

class ScheduleViewModelFactory(private val repository: ScheduleRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ScheduleViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
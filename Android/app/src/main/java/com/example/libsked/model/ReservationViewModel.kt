package com.example.libsked.model

import androidx.lifecycle.*
import com.example.libsked.repository.ReservationRepository
import kotlinx.coroutines.launch

class ReservationViewModel(private val repository: ReservationRepository) : ViewModel() {
    val allReservation:  LiveData<List<Reservation>> = repository.allReservations.asLiveData()

    fun insert(reservation: Reservation) = viewModelScope.launch {
        repository.insert(reservation)
    }
}

class ReservationViewModelFactory(private val repository: ReservationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReservationViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ReservationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
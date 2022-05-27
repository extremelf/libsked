package com.example.libsked.repository

import androidx.annotation.WorkerThread
import com.example.libsked.dao.ReservationDao
import com.example.libsked.model.Reservation
import kotlinx.coroutines.flow.Flow

class ReservationRepository(private val reservationDao: ReservationDao) {
    val allReservations: Flow<List<Reservation>> = reservationDao.getReservations()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(reservation: Reservation){
        reservationDao.insert(reservation)
    }
}
package com.example.libsked.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.example.libsked.model.Reservation
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * FROM user_reservation")
    fun getReservations(): Flow<List<Reservation>>

    @Insert(onConflict = IGNORE, entity = Reservation::class)
    fun insert(reservation: Reservation)

    @Delete
    suspend fun delete(reservation: Reservation)

    @Query("DELETE FROM user_reservation")
    suspend fun deleteAll()
}
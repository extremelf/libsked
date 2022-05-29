package com.example.libsked.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.example.libsked.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule WHERE room_id = :roomId")
    fun getRoomSchedule(roomId: Int): Flow<List<Schedule>>

    @Query("SELECT * FROM schedule")
    fun getAllSchedules(): Flow<List<Schedule>>

    /*@Query("SELECT CASE WHEN EXISTS(SELECT * FROM schedule WHERE NOW() BETWEEN start AND `end` AND room_id = :roomId) THEN 1 ELSE 0 end")
    fun roomOccupied(roomId: Int): Boolean*/

    @Query("SELECT DISTINCT(room_id) as rooms FROM schedule")
    fun getRooms(): Flow<List<Int>>

    @Insert(onConflict = IGNORE)
    suspend fun insert(schedule: Schedule)

    @Delete
    suspend fun delete(schedule: Schedule)

    @Query("DELETE FROM schedule")
    suspend fun deleteAll()
}
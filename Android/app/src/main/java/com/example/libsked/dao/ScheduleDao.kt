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
    @Query("SELECT * FROM room_schedule WHERE room_id = :roomId")
    fun getRoomSchedule(roomId: Int): Flow<List<Schedule>>

    @Query("SELECT * FROM room_schedule")
    fun getAllSchedules(): Flow<List<Schedule>>

    /*@Query("SELECT CASE WHEN EXISTS(SELECT * FROM schedule WHERE NOW() BETWEEN start AND `end` AND room_id = :roomId) THEN 1 ELSE 0 end")
    fun roomOccupied(roomId: Int): Boolean*/


    @Query("SELECT * FROM ROOM_SCHEDULE WHERE room_id = :id AND " +
            "start > strftime('%s',datetime('now', 'start of day'))*1000 AND " +
            "`end` < strftime('%s',datetime('now', 'start of day', '+1 day', '-1 second'))*1000")
    fun getDayScheduleOfRoom(id: Int): Flow<List<Schedule>>


    @Query("SELECT * FROM ROOM_SCHEDULE WHERE person_id = :userID AND start > :startOfday AND `end` < :endOfday")
    fun getSchedulexDay(startOfday: Long,endOfday: Long,userID: String): Flow<List<Schedule>>

    @Query("SELECT DISTINCT(room_id) as rooms FROM room_schedule")
    fun getRooms(): Flow<List<Int>>

    @Insert(onConflict = IGNORE)
    suspend fun insert(schedule: Schedule)

    @Delete
    suspend fun delete(schedule: Schedule)

    @Query("DELETE FROM room_schedule")
    suspend fun deleteAll()
}
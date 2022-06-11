package com.example.libsked.repository

import androidx.annotation.WorkerThread
import com.example.libsked.dao.RoomDao
import com.example.libsked.dao.ScheduleDao
import com.example.libsked.model.RoomTable
import com.example.libsked.model.Schedule
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(
    private val scheduleDao: ScheduleDao,
    private val roomDao: RoomDao,
) {
    val allSchedules: Flow<List<Schedule>> = scheduleDao.getAllSchedules()

    fun getRoomSchedule(roomId: Int): Flow<List<Schedule>> = scheduleDao.getRoomSchedule(roomId)

    fun getRoomScheduleOfDay(roomId: Int): Flow<List<Schedule>> = scheduleDao.getDayScheduleOfRoom(roomId)

    fun getRoom(id: Int): Flow<RoomTable> = roomDao.getRoom(id)

    fun getRooms(): Flow<List<Int>> = roomDao.getRoomNumbers()

    /*fun isRoomOccupied(roomId: Int): Boolean{
        return scheduleDao.roomOccupied(roomId)
    }*/

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(schedule: Schedule) {
        scheduleDao.insert(schedule)
    }
}
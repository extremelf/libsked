package com.example.libsked.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.libsked.dao.ScheduleDao
import com.example.libsked.model.Schedule
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val scheduleDao: ScheduleDao) {
    val allSchedules: Flow<List<Schedule>> = scheduleDao.getAllSchedules()

    fun getRoomSchedule(roomId: Int): Flow<List<Schedule>> {
        return scheduleDao.getRoomSchedule(roomId)
    }

    fun isRoomOccupied(roomId: Int): Boolean{
        return scheduleDao.roomOccupied(roomId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(schedule: Schedule){
        scheduleDao.insert(schedule)
    }
}
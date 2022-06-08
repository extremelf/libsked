package com.example.libsked.appplication

import android.app.Application
import com.example.libsked.db.ScheduleRoomDatabase
import com.example.libsked.repository.ScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ScheduleApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { ScheduleRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {
        ScheduleRepository(database.ScheduleDao(), database.RoomDao(), database.PersonDao())
    }
}
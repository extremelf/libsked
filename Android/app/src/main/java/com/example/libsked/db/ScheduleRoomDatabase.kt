package com.example.libsked.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.libsked.dao.ScheduleDao
import com.example.libsked.model.Schedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.*

@Database(entities = [Schedule::class], version = 1, exportSchema = false)
abstract class ScheduleRoomDatabase: RoomDatabase() {
    abstract fun ScheduleDao(): ScheduleDao

    private class ScheduleDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase){
            super.onOpen(db)
        }

        override fun onCreate(db: SupportSQLiteDatabase){
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch{
                    populateDatabase(database.ScheduleDao())
                }
            }
        }

        suspend fun populateDatabase(scheduleDao: ScheduleDao){
            scheduleDao.deleteAll()

            val schedule = Schedule(1, 3,
                Timestamp(2022,5,27,10,30,0,0),
                Timestamp(2022,5,27,11,0,0,0))
            scheduleDao.insert(schedule)
            val schedule2 = Schedule(1, 1,
                Timestamp(2022,5,27,10,30,0,0),
                Timestamp(2022,5,27,11,0,0,0))
            scheduleDao.insert(schedule2)
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ScheduleRoomDatabase? = null

        fun getDatabase(context: Context, scope:CoroutineScope): ScheduleRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScheduleRoomDatabase::class.java,
                    "Schedule_database"
                )
                    .addCallback(ScheduleDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
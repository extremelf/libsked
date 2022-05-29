package com.example.libsked.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.libsked.dao.ScheduleDao
import com.example.libsked.model.Schedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Timestamp

import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?):
            java.sql.Date {
        return java.sql.Date(value ?: 0)
    }

    @TypeConverter
    fun dateToTimestamp(date: java.sql.Date?)
            : Long {
        return date?.getTime() ?: 0
    }
}

@Database(entities = [Schedule::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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

            var schedule = Schedule(1, 2,
                Timestamp.valueOf("2022-5-28 10:30:0.0").time,
                Timestamp.valueOf("2022-5-28 18:0:0.0").time)
            scheduleDao.insert(schedule)
            schedule = Schedule(2, 3,
                Timestamp.valueOf("2022-5-28 10:30:0.0").time,
                Timestamp.valueOf("2022-5-28 18:0:0.0").time)
            scheduleDao.insert(schedule)
            schedule = Schedule(3, 4,
                Timestamp.valueOf("2022-5-28 10:30:0.0").time,
                Timestamp.valueOf("2022-5-28 18:0:0.0").time)
            scheduleDao.insert(schedule)
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
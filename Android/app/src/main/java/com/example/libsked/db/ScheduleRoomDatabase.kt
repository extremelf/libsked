package com.example.libsked.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.libsked.dao.RoomDao
import com.example.libsked.dao.ScheduleDao
import com.example.libsked.model.RoomTable
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

@Database(
    entities = [Schedule::class, RoomTable::class],
    version = 3,
    exportSchema = false

)
@TypeConverters(Converters::class)
abstract class ScheduleRoomDatabase : RoomDatabase() {
    abstract fun ScheduleDao(): ScheduleDao
    abstract fun RoomDao(): RoomDao

    private class ScheduleDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(
                        database.ScheduleDao(),
                        database.RoomDao(),
                    )
                }
            }
        }

        suspend fun populateDatabase(
            scheduleDao: ScheduleDao,
            roomDao: RoomDao,
        ) {
            scheduleDao.deleteAll()
            roomDao.deleteAll()

            var room = RoomTable(
                roomNumber = 1,
                chairsNumber = 4,
                socketsNumber = 3,
                tablesNumber = 3,
                description = "Small room"
            )
            val room1Id = roomDao.insert(room)
            room = RoomTable(
                roomNumber = 2,
                chairsNumber = 15,
                socketsNumber = 9,
                tablesNumber = 1,
                description = "Big room"
            )
            val room2Id = roomDao.insert(room)
            room = RoomTable(
                roomNumber = 3,
                chairsNumber = 15,
                socketsNumber = 9,
                tablesNumber = 4,
                description = "Big room"
            )
            roomDao.insert(room)
            room = RoomTable(
                roomNumber = 4,
                chairsNumber = 15,
                socketsNumber = 9,
                tablesNumber = 2,
                description = "Big room"
            )
            roomDao.insert(room)

            room = RoomTable(
                id = 99,
                roomNumber = 99,
                chairsNumber = 100,
                socketsNumber = 300,
                tablesNumber = 40,
                description = "Library"
            )
            roomDao.insert(room)
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: ScheduleRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ScheduleRoomDatabase {
            return INSTANCE ?: synchronized(this) {
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
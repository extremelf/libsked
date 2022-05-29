package com.example.libsked.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.libsked.dao.PersonDao
import com.example.libsked.dao.RoomDao
import com.example.libsked.dao.ScheduleDao
import com.example.libsked.model.Person
import com.example.libsked.model.RoomTable
import com.example.libsked.model.Schedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Timestamp

import java.util.*

@Database(
    entities = [Schedule::class, RoomTable::class, Person::class],
    version = 1,
    exportSchema = false
)
abstract class ScheduleRoomDatabase : RoomDatabase() {
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
    abstract fun PersonDao(): PersonDao
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
                        database.PersonDao()
                    )
                }
            }
        }

        suspend fun populateDatabase(
            scheduleDao: ScheduleDao,
            roomDao: RoomDao,
            personDao: PersonDao
        ) {
            scheduleDao.deleteAll()
            personDao.deleteAll()
            roomDao.deleteAll()

            val room1 = RoomTable(
                roomNumber = 1,
                chairsNumber = 4,
                socketsNumber = 3,
                description = "Small room"
            )
            val room2 = RoomTable(
                roomNumber = 2,
                chairsNumber = 15,
                socketsNumber = 9,
                description = "Big room"
            )
            val room1Id = roomDao.insert(room1)
            val room2Id = roomDao.insert(room2)

            val person = Person(name = "Luís", cc = 12313123)

            val personId = personDao.insert(person)

            val schedule = Schedule(
                creation_timestamp = Timestamp(Calendar.getInstance().timeInMillis),
                roomId = room1Id.toInt(),
                personId = personId.toInt(),
                start = Timestamp(2022, 5, 27, 10, 30, 0, 0),
                end = Timestamp(2022, 5, 27, 11, 0, 0, 0)
            )
            /*
            var schedule = Schedule(1, 2,
                Timestamp.valueOf("2022-5-28 10:30:0.0").time,
                Timestamp.valueOf("2022-5-28 18:0:0.0").time)
            scheduleDao.insert(schedule)
            schedule = Schedule(2, 3,
                Timestamp.valueOf("2022-5-28 10:30:0.0").time,
                Timestamp.valueOf("2022-5-28 18:0:0.0").time)
            scheduleDao.insert(schedule)
             */
            val schedule2 = Schedule(
                creation_timestamp = Timestamp(Calendar.getInstance().timeInMillis),
                roomId = room2Id.toInt(),
                personId = personId.toInt(),
                start = Timestamp(2022, 5, 27, 10, 30, 0, 0),
                end = Timestamp(2022, 5, 27, 11, 0, 0, 0)
            )
            scheduleDao.insert(schedule2)
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
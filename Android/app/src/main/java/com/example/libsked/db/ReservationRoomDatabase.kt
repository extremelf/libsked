package com.example.libsked.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.libsked.dao.ReservationDao
import com.example.libsked.model.Reservation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Reservation::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ReservationRoomDatabase: RoomDatabase() {
    abstract fun ReservationDao(): ReservationDao

    private class ReservationDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase){
            super.onOpen(db)
        }
        override fun onCreate(db: SupportSQLiteDatabase){
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.ReservationDao())
                }
            }
        }

        suspend fun populateDatabase(reservationDao: ReservationDao){
            reservationDao.deleteAll()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ReservationRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ReservationRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReservationRoomDatabase::class.java,
                    "User_reservation_database"
                )
                    .addCallback(ReservationDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
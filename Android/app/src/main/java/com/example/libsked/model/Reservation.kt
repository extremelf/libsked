package com.example.libsked.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.libsked.db.Converters
import java.sql.Timestamp

@Entity(tableName = "user_reservation")
class Reservation (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "room_id")
    val roomId: Int,
    @ColumnInfo(name = "start")
    @TypeConverters(Converters::class)
    val start: Long,
    @ColumnInfo(name = "end")
    @TypeConverters(Converters::class)
    val end: Long
)
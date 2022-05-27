package com.example.libsked.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "user_reservation")
class Reservation (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "room_id")
    val roomId: Int,
    @ColumnInfo(name = "start")
    val start: Timestamp,
    @ColumnInfo(name = "end")
    val end: Timestamp
)
package com.example.libsked.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room")
class RoomTable (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "room_number")
    val roomNumber: Int,
    @ColumnInfo(name = "chairs_number")
    val chairsNumber: Int,
    @ColumnInfo(name = "sockets_number")
    val socketsNumber: Int,
    @ColumnInfo(name = "description")
    val description: String
)
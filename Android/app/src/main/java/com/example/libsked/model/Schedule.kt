package com.example.libsked.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Timestamp
import com.example.libsked.model.RoomTable

@Entity(
    tableName = "room_schedule", foreignKeys = [ForeignKey(
        entity = RoomTable::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("room_id"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Person::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("person_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class Schedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "creation_timestamp")
    val creation_timestamp: Timestamp,
    @ColumnInfo(name = "person_id")
    val personId: Int,
    @ColumnInfo(name = "room_id")
    val roomId: Int,
    @ColumnInfo(name = "start")
    val start: Timestamp,
    @ColumnInfo(name = "end")
    val end: Timestamp
)
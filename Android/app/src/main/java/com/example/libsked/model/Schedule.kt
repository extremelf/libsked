package com.example.libsked.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.libsked.db.Converters

@Entity(
    tableName = "room_schedule", foreignKeys = [ForeignKey(
        entity = RoomTable::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("room_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class Schedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "creation_timestamp")
    @TypeConverters(Converters::class)
    val creation_timestamp: Long,
    @ColumnInfo(name = "person_id")
    val personId: String = "",
    @ColumnInfo(name = "room_id")
    val roomId: Int,
    @ColumnInfo(name = "start")
    @TypeConverters(Converters::class)
    val start: Long,
    @ColumnInfo(name = "end")
    @TypeConverters(Converters::class)
    val end: Long
)
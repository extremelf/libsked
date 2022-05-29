package com.example.libsked.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.example.libsked.model.RoomTable
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Query("SELECT * FROM room WHERE id = :id")
    fun getRoom(id: Int): Flow<RoomTable>

    @Query("SELECT * FROM room")
    fun getAllRooms(): Flow<List<RoomTable>>

    @Insert(onConflict = IGNORE)
    suspend fun insert(room: RoomTable): Long

    @Delete
    suspend fun delete(room: RoomTable)

    @Query("DELETE FROM room")
    suspend fun deleteAll()
}
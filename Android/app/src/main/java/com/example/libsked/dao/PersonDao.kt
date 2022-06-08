package com.example.libsked.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.example.libsked.model.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM person WHERE id = :id")
    fun getPerson(id: Int): Flow<Person>

    @Query("SELECT * FROM person")
    fun getPeople(): Flow<List<Person>>

    @Insert(onConflict = IGNORE)
    suspend fun insert(person: Person): Long

    @Delete
    suspend fun delete(person: Person)

    @Query("DELETE FROM person")
    suspend fun deleteAll()
}
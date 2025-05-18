package com.example.sem2androidproject.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg notes: Note): List<Long>


    @Delete
    suspend fun delete(note: Note)
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): Category?
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): Note?

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * FROM notes")
    suspend fun getAll(): List<Note>
}
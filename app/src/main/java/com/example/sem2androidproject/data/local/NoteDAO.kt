package com.example.sem2androidproject.data.local

import androidx.room.ColumnInfo
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

    @Query(
        """
    SELECT category_id, SUM(amount) 
    FROM notes 
    WHERE entry_type = :entryType AND date BETWEEN :start AND :end 
    GROUP BY category_id
"""
    )
    suspend fun getCategorySums(entryType: EntryType, start: Long, end: Long): List<CategorySum>

    data class CategorySum(
        @ColumnInfo(name = "category_id") val categoryId: Long,
        @ColumnInfo(name = "SUM(amount)") val total: Double
    )


    @Query("SELECT SUM(amount) FROM notes WHERE entry_type = :entryType AND date BETWEEN :start AND :end")
    suspend fun getTotalByType(entryType: EntryType, start: Long, end: Long): Double

    @Query(
        """
    SELECT strftime('%Y-%m', datetime(date / 1000, 'unixepoch')) AS month, 
           SUM(amount) 
    FROM notes 
    WHERE entry_type = :entryType 
    GROUP BY month
"""
    )
    suspend fun getMonthlySums(entryType: EntryType): List<DateSum>

    data class DateSum(
        @ColumnInfo(name = "month") val period: String,
        @ColumnInfo(name = "SUM(amount)") val total: Double
    )

}
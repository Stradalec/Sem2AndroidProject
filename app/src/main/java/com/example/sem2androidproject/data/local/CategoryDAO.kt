package com.example.sem2androidproject.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDAO {
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): Category?
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)


    @Query("SELECT * FROM categories WHERE type = :type")
    suspend fun getCategoriesByType(type: EntryType): List<Category>


    @Delete
    suspend fun delete(category: Category)
}

package com.example.sem2androidproject.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "body") val noteBody: String,
    @ColumnInfo(name = "date") val noteDate: Long,
    @ColumnInfo(name = "reminder_time") val reminderTime: Long?,
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "entry_type") val type: EntryType
)
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: EntryType
)
enum class EntryType { EXPENSE, INCOME }

package com.example.sem2androidproject.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "body") val noteBody: String,
    @ColumnInfo(name = "date") val noteDate: Long,
    @ColumnInfo(name = "reminder_time") val reminderTime: Long?
)
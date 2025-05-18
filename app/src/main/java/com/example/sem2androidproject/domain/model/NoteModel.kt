package com.example.sem2androidproject.domain.model

import androidx.room.ColumnInfo
import com.example.sem2androidproject.data.local.EntryType
import java.io.Serializable
import java.util.Date


data class NoteModel(
    val id: Long = 0,
    val amount: Double,
    val noteBody: String,
    val noteDate: Long,
    val reminderTime: Long? = null,
    val categoryId: Long,
    val type: EntryType
) : Serializable
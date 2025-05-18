package com.example.sem2androidproject.data

import androidx.room.TypeConverter
import com.example.sem2androidproject.data.local.Note
import com.example.sem2androidproject.domain.model.NoteModel
import java.util.Date

fun Note.toDomain(): NoteModel {
    return NoteModel(
        id = this.id,
        amount = this.amount ?: 0.0,
        category = this.category ?: "Без категории",
        noteBody = this.noteBody,
        noteDate = this.noteDate,
        reminderTime = this.reminderTime
    )
}

fun NoteModel.toEntity(): Note {
    return Note(
        id = this.id,
        amount = this.amount,
        category = this.category,
        noteBody = this.noteBody,
        noteDate = this.noteDate,
        reminderTime = this.reminderTime
    )
}
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
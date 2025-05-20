package com.example.sem2androidproject.data

import androidx.room.TypeConverter
import com.example.sem2androidproject.data.local.Category
import com.example.sem2androidproject.data.local.Note
import com.example.sem2androidproject.domain.model.CategoryModel
import com.example.sem2androidproject.domain.model.NoteModel
import java.util.Date

fun Note.toDomain(): NoteModel {
    return NoteModel(
        id = this.id,
        amount = this.amount,
        noteBody = this.noteBody,
        noteDate = this.noteDate,
        reminderTime = this.reminderTime,
        categoryId = this.categoryId,
        type = this.type
    )
}

fun NoteModel.toEntity(): Note {
    return Note(
        id = this.id,
        amount = this.amount,
        noteBody = this.noteBody,
        noteDate = this.noteDate,
        reminderTime = this.reminderTime,
        categoryId = this.categoryId,
        type = this.type
    )
}
fun Category.toDomain(): CategoryModel{
    return CategoryModel(
        id = this.id,
        name = this.name,
        type = this.type
    )
}
fun CategoryModel.toEntity(): Category{
    return Category(
        id = this.id,
        name = this.name,
        type = this.type
    )
}


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
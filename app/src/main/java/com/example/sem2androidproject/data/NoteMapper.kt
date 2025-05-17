package com.example.sem2androidproject.data

import com.example.sem2androidproject.data.local.Note
import com.example.sem2androidproject.domain.model.NoteModel

fun Note.toDomain(): NoteModel {
    return NoteModel(
        id = this.id,
        title = this.title ?: "Без названия",
        category = this.category ?: "Без категории",
        noteBody = this.noteBody
    )
}

fun NoteModel.toEntity(): Note {
    return Note(
        id = this.id,
        title = this.title,
        category = this.category,
        noteBody = this.noteBody
    )
}
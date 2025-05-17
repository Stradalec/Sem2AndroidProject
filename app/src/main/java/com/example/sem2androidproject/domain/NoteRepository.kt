package com.example.sem2androidproject.domain

import com.example.sem2androidproject.domain.model.NoteModel

interface INoteRepository {
    suspend fun addDefaultNote()
    suspend fun getAllNotes(): List<NoteModel>
    suspend fun insetNote(note: NoteModel)
    suspend fun deleteNote(note: NoteModel)
}
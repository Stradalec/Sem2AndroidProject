package com.example.sem2androidproject.domain

import com.example.sem2androidproject.data.local.EntryType
import com.example.sem2androidproject.data.local.NoteDAO
import com.example.sem2androidproject.domain.model.NoteModel
import java.util.Date

interface INoteRepository {
    suspend fun addDefaultNote()
    suspend fun getAllNotes(): List<NoteModel>
    suspend fun insetNote(note: NoteModel)
    suspend fun getNoteById(id: Long): NoteModel?
    suspend fun updateNote(note: NoteModel)
    suspend fun deleteNote(note: NoteModel)
    suspend fun getCategorySums(type: EntryType, start: Date, end: Date): List<NoteDAO.CategorySum>
    suspend fun getMonthlyReport(type: EntryType): List<NoteDAO.DateSum>
}


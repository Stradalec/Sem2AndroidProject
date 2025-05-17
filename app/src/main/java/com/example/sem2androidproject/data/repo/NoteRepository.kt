package com.example.sem2androidproject.data.repo

import com.example.sem2androidproject.data.local.NoteDAO
import com.example.sem2androidproject.data.toDomain
import com.example.sem2androidproject.data.toEntity
import com.example.sem2androidproject.domain.INoteRepository
import com.example.sem2androidproject.domain.model.NoteModel
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDAO: NoteDAO) : INoteRepository {
    override suspend fun addDefaultNote(){
        val defaultNote =
            NoteModel(id = 0, title = "Спасательный круг", category = "Затычка", noteBody = "Без меня всё развалится")
        noteDAO.insertAll(defaultNote.toEntity())
    }

    override suspend fun getAllNotes(): List<NoteModel> =
        noteDAO.getAll().map { it.toDomain() }

    override suspend fun insetNote(note: NoteModel) {
        noteDAO.insertAll(note.toEntity())
    }

    override suspend fun deleteNote(note: NoteModel) =
        noteDAO.delete(note.toEntity())
}
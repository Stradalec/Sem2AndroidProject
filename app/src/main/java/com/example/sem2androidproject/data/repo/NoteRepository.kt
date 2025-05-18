package com.example.sem2androidproject.data.repo

import com.example.sem2androidproject.data.local.NoteDAO
import com.example.sem2androidproject.data.toDomain
import com.example.sem2androidproject.data.toEntity
import com.example.sem2androidproject.domain.INoteRepository
import com.example.sem2androidproject.domain.model.NoteModel
import java.util.Date
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDAO: NoteDAO) : INoteRepository {
    override suspend fun addDefaultNote(){
        val defaultNote =
            NoteModel(id = 0, title = "Спасательный круг", category = "Затычка", noteBody = "Без меня всё развалится", noteDate = Date().time )
        noteDAO.insertAll(defaultNote.toEntity())
    }

    override suspend fun getAllNotes(): List<NoteModel> =
        noteDAO.getAll().map { it.toDomain() }

    override suspend fun insetNote(note: NoteModel) {
        noteDAO.insertAll(note.toEntity())
    }

    override suspend fun getNoteById(id: Long): NoteModel? {
        return noteDAO.getNoteById(id)?.toDomain()
    }
    override suspend fun updateNote(note: NoteModel) {
        noteDAO.update(note.toEntity())
    }

    override suspend fun deleteNote(note: NoteModel) =
        noteDAO.delete(note.toEntity())
}
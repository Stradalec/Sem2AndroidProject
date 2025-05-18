package com.example.sem2androidproject.data.repo

import com.example.sem2androidproject.data.local.EntryType
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
            NoteModel(id = 0, amount = 300.0, noteBody = "Без меня всё развалится", noteDate = Date().time, categoryId = 0, type = EntryType.EXPENSE )
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

    override suspend fun getCategorySums(
        type: EntryType,
        start: Date,
        end: Date
    ): List<NoteDAO.CategorySum> {
        return noteDAO.getCategorySums(
            entryType = type,
            start = start.time,
            end = end.time
        )
    }


    override suspend fun getMonthlyReport(type: EntryType): List<NoteDAO.DateSum> {
        return noteDAO.getMonthlySums(entryType = type)
    }

}
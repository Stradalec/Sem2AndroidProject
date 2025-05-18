package com.example.sem2androidproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sem2androidproject.domain.INoteRepository
import com.example.sem2androidproject.domain.model.NoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: INoteRepository) : ViewModel() {
    private val _notes = MutableLiveData<List<NoteModel>>()
    val notes: LiveData<List<NoteModel>> = _notes

    init {
        viewModelScope.launch(Dispatchers.IO) {
            addDefaultNoteIfNeeded()
            loadNotes()
        }
    }

    private suspend fun addDefaultNoteIfNeeded() {
        withContext(Dispatchers.IO) {
            val allNotes = repository.getAllNotes()
            if (allNotes.isEmpty()) {
                repository.addDefaultNote()
            }
        }
    }

    fun loadNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _notes.postValue(repository.getAllNotes())
            } catch (e: Exception) {

                _notes.postValue( null)
            }
        }
    }

    fun addNote(note: NoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insetNote(note)
            loadNotes()
        }
    }

    fun deleteNote(note: NoteModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
            loadNotes()
        }
    }

    fun updateNote(note: NoteModel) {
        viewModelScope.launch {
            repository.updateNote(note)
            loadNotes()
        }

    }

}
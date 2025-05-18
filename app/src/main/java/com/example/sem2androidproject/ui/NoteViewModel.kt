package com.example.sem2androidproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sem2androidproject.data.local.EntryType
import com.example.sem2androidproject.data.local.NoteDAO
import com.example.sem2androidproject.domain.ICategoryRepository
import com.example.sem2androidproject.domain.INoteRepository
import com.example.sem2androidproject.domain.model.NoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: INoteRepository, private val categoryRepository: ICategoryRepository) : ViewModel() {
    private val _notes = MutableLiveData<List<NoteModel>>()
    val notes: LiveData<List<NoteModel>> = _notes
    private val _categoryName = MutableLiveData<String>()
    private val _categorySums = MutableLiveData<List<NoteDAO.CategorySum>>()
    val categorySums: LiveData<List<NoteDAO.CategorySum>> = _categorySums

    fun loadCategorySums(type: EntryType, start: Date, end: Date) {
        viewModelScope.launch {
            _categorySums.value = repository.getCategorySums(type, start, end)
        }
    }

    fun getCategoryNameById(id: Long): LiveData<String> {
        viewModelScope.launch {
            val category = withContext(Dispatchers.IO) {
                categoryRepository.getCategoryById(id)
            }
            _categoryName.value = category?.name ?: "Неизвестно"
        }
        return _categoryName
    }
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
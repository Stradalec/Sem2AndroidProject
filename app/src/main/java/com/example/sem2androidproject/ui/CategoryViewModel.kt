package com.example.sem2androidproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sem2androidproject.data.local.Category
import com.example.sem2androidproject.data.local.EntryType
import com.example.sem2androidproject.domain.ICategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: ICategoryRepository
) : ViewModel() {
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    fun loadCategoriesByType(type: EntryType) {
        viewModelScope.launch {
            _categories.value = repository.getCategoriesByType(type)
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.addCategory(category)
            loadCategoriesByType(category.type)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
            loadCategoriesByType(category.type)
        }
    }
}


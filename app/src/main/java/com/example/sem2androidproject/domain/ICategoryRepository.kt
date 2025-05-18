package com.example.sem2androidproject.domain

import com.example.sem2androidproject.data.local.Category
import com.example.sem2androidproject.data.local.EntryType

interface ICategoryRepository {
    suspend fun addCategory(category: Category)
    suspend fun getCategoriesByType(type: EntryType): List<Category>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun deleteCategory(category: Category)
}
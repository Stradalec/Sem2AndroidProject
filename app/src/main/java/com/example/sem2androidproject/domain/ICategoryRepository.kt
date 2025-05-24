package com.example.sem2androidproject.domain

import com.example.sem2androidproject.data.local.EntryType
import com.example.sem2androidproject.domain.model.CategoryModel

interface ICategoryRepository {
    suspend fun addCategory(category: CategoryModel)
    suspend fun getCategoriesByType(type: EntryType): List<CategoryModel>
    suspend fun getCategoryById(id: Long): CategoryModel?
    suspend fun deleteCategory(category: CategoryModel)
}
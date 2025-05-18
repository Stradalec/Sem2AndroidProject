package com.example.sem2androidproject.data.repo

import com.example.sem2androidproject.data.local.Category
import com.example.sem2androidproject.data.local.CategoryDAO
import com.example.sem2androidproject.data.local.EntryType
import com.example.sem2androidproject.domain.ICategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDAO
) : ICategoryRepository {
    override suspend fun addCategory(category: Category) = categoryDao.insert(category)
    override suspend fun getCategoryById(id: Long): Category? =
        categoryDao.getCategoryById(id)

    override suspend fun getCategoriesByType(type: EntryType) = categoryDao.getCategoriesByType(type)
    override suspend fun deleteCategory(category: Category) = categoryDao.delete(category)
}

package com.example.sem2androidproject.data.repo

import com.example.sem2androidproject.data.local.CategoryDAO
import com.example.sem2androidproject.data.local.EntryType
import com.example.sem2androidproject.data.toDomain
import com.example.sem2androidproject.data.toEntity
import com.example.sem2androidproject.domain.ICategoryRepository
import com.example.sem2androidproject.domain.model.CategoryModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDAO
) : ICategoryRepository {
    override suspend fun addCategory(category: CategoryModel) =
        categoryDao.insert(category.toEntity())

    override suspend fun getCategoryById(id: Long): CategoryModel? =
        categoryDao.getCategoryById(id)?.toDomain()

    override suspend fun getCategoriesByType(type: EntryType): List<CategoryModel> =
        (categoryDao.getCategoriesByType(type).map { it.toDomain() })

    override suspend fun deleteCategory(category: CategoryModel) =
        categoryDao.delete(category.toEntity())
}

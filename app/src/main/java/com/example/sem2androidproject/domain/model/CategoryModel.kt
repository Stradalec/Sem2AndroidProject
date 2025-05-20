package com.example.sem2androidproject.domain.model

import com.example.sem2androidproject.data.local.EntryType
import java.io.Serializable


data class CategoryModel(
    val id: Long = 0,
    val name: String,
    val type: EntryType
) : Serializable

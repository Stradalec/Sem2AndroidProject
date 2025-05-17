package com.example.sem2androidproject.domain.model

import java.util.Date

data class NoteModel(
    val id: Long = 0,
    val title: String,
    val category: String,
    val noteBody: String,
    val noteDate: Date
)
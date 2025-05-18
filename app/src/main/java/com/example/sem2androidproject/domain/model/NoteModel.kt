package com.example.sem2androidproject.domain.model

import java.io.Serializable
import java.util.Date


data class NoteModel(
    val id: Long = 0,
    val amount: Double,
    val category: String,
    val noteBody: String,
    val noteDate: Long,
    val reminderTime: Long? = null
) : Serializable
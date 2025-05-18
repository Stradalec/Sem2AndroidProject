package com.example.sem2androidproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sem2androidproject.data.Converters

@Database(entities = [Note::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao() : NoteDAO

}
package com.example.sem2androidproject.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.sem2androidproject.data.local.CategoryDAO
import com.example.sem2androidproject.data.local.NoteDAO
import com.example.sem2androidproject.data.local.NoteDatabase
import com.example.sem2androidproject.data.repo.CategoryRepository
import com.example.sem2androidproject.data.repo.NoteRepository
import com.example.sem2androidproject.domain.ICategoryRepository
import com.example.sem2androidproject.domain.INoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NoteDatabaseModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(context, NoteDatabase::class.java, "note_database")
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDAO {
        return database.noteDao()
    }

    @Provides
    fun provideCategoryDao(database: NoteDatabase): CategoryDAO = database.categoryDao()

}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNoteRepository(noteDAO: NoteDAO): INoteRepository {
        return NoteRepository(noteDAO)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDAO: CategoryDAO): ICategoryRepository {
        return CategoryRepository(categoryDAO)
    }
}

@HiltAndroidApp
class NoteApplication : Application() {
    @Inject
    lateinit var database: NoteDatabase
}
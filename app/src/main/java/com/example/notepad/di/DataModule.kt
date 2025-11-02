package com.example.notepad.di

import android.content.Context
import com.example.notepad.data.NotesDao
import com.example.notepad.data.NotesDatabase
import com.example.notepad.data.NotesRepositoryImpl
import com.example.notepad.domain.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindNoteRepository(
        impl: NotesRepositoryImpl
    ): NoteRepository

    companion object{
        @Singleton
        @Provides
        fun provideDataBase(
            @ApplicationContext context: Context
        ): NotesDatabase{
            return NotesDatabase.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideNotesDao(
            dataBase: NotesDatabase
        ): NotesDao {
            return dataBase.notesDao()
        }
    }


}
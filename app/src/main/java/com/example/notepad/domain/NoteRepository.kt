package com.example.notepad.domain

import androidx.compose.runtime.Updater
import kotlinx.coroutines.flow.Flow
import java.time.temporal.TemporalQuery

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>

    suspend fun addNote(
        title: String,
        content: String,
        isPinned: Boolean,
        updateAt: Long
    )

    suspend fun deleteNote(noteId: Int)

    suspend fun editNote(note: Note)

    fun searchNote(query: String): Flow<List<Note>>

    suspend fun switchPinnedStatus(noteId: Int)


    suspend fun getNote(noteId: Int): Note
}
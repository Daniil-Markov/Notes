package com.example.notepad.domain.repository

import com.example.notepad.domain.entity.ContentItem
import com.example.notepad.domain.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>

    suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updateAt: Long
    )

    suspend fun deleteNote(noteId: Int)

    suspend fun editNote(note: Note)

    fun searchNote(query: String): Flow<List<Note>>

    suspend fun switchPinnedStatus(noteId: Int)


    suspend fun getNote(noteId: Int): Note
}
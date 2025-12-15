package com.example.notepad.domain

import android.icu.text.CaseMap
import kotlinx.coroutines.flow.Flow
import java.time.temporal.TemporalQuery

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>

    fun addNote(title: String, content: String)

    fun deleteNote(noteId: Int)

    fun editNote(note: Note)

    fun searchNote(query: String): Flow<List<Note>>

    fun switchPinnedStatus(noteId: Int)


    fun getNote(noteId: Int): Note
}
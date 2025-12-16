package com.example.notepad.data.repository

import com.example.notepad.data.NotesDao
import com.example.notepad.data.mapper.toDbModel
import com.example.notepad.data.mapper.toEntities
import com.example.notepad.data.mapper.toEntity
import com.example.notepad.domain.entity.ContentItem
import com.example.notepad.domain.entity.Note
import com.example.notepad.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
): NoteRepository {


    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map {
            it.toEntities()
        }
    }

    override suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updateAt: Long
    ) {
        val note = Note(0, title, content, updateAt, isPinned)
        val noteDbModel  = note.toDbModel()
        notesDao.addNote(noteDbModel)
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toDbModel())
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map { it.toEntities() }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

}
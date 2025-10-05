package com.example.notepad.data

import androidx.compose.runtime.currentRecomposeScope
import com.example.notepad.domain.Note
import com.example.notepad.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object NotesRepositoryImpl: NoteRepository {

    private val notesListFlow = MutableStateFlow<List<Note>>(listOf())

    override fun getAllNotes(): Flow<List<Note>> {
        return notesListFlow.asStateFlow()
    }


    override fun deleteNote(noteId: Int) {
        notesListFlow.update { oldList ->
            oldList.toMutableList().apply {
                removeIf { it.noteId == noteId }
            }
        }
    }

    override fun editNote(note: Note) {
        notesListFlow.update{ oldList ->
            oldList.map{
                if(it.noteId == note.noteId){
                    note
                }else{
                    it
                }
            }
        }
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return notesListFlow.map { currentList ->
            currentList.filter {
                it.title.contains(query) || it.content.contains(query)
            }
        }

    }

    override fun addNote(title: String, content: String) {
        notesListFlow.update { oldList ->
            val note = Note(
                noteId = oldList.size,
                title = title,
                content = content,
                updateAt = System.currentTimeMillis(),
                isPinned = false
            )
            oldList + note
        }
    }

    override fun switchPinnedStatus(noteId: Int) {
        notesListFlow.update{ oldList ->
            oldList.map{
                if(it.noteId == noteId){
                    it.copy(isPinned = !it. isPinned)
                }else{
                    it
                }
            }
        }
    }

    override fun getNote(noteId: Int): Note {
        return notesListFlow.value.first{
            it.noteId == noteId
        }
    }
}
package com.example.notepad.domain

import kotlinx.coroutines.flow.Flow

class SearchNoteUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(query: String): Flow<List<Note>>{
        return repository.searchNote(query)
    }
}
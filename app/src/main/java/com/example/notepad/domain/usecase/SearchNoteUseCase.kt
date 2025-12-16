package com.example.notepad.domain.usecase

import com.example.notepad.domain.entity.Note
import com.example.notepad.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(query: String): Flow<List<Note>>{
        return repository.searchNote(query)
    }
}
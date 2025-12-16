package com.example.notepad.domain.usecase

import com.example.notepad.domain.entity.Note
import com.example.notepad.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int): Note {
        return repository.getNote(noteId = noteId)
    }
}
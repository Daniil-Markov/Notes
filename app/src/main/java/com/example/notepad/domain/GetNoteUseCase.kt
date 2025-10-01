package com.example.notepad.domain

class GetNoteUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(noteId: Int): Note{
        repository.getNote(noteId = noteId)
    }
}
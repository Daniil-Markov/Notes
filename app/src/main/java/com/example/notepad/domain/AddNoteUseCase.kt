package com.example.notepad.domain

class AddNoteUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(note: Note) {
        repository.addNote(note = note)
    }
}
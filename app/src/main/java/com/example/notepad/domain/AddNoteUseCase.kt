package com.example.notepad.domain

class AddNoteUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(
        title: String,
        content: String
    ) {
        repository.addNote(title = title, content = content)
    }
}
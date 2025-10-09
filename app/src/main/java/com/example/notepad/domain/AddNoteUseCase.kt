package com.example.notepad.domain

class AddNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(
        title: String,
        content: String
    ) {
        repository.addNote(
            title = title,
            content = content,
            isPinned = false,
            updateAt = System.currentTimeMillis()
        )
    }
}
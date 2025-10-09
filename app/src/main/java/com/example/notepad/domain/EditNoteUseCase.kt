package com.example.notepad.domain

class EditNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.editNote(note.copy(updateAt = System.currentTimeMillis()))
    }
}
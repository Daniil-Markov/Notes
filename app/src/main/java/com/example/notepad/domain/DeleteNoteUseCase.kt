package com.example.notepad.domain

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int){
        repository.deleteNote(noteId = noteId)
    }
}
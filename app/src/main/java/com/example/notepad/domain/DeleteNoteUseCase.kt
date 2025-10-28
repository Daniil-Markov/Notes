package com.example.notepad.domain

import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int){
        repository.deleteNote(noteId = noteId)
    }
}
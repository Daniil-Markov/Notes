package com.example.notepad.domain

import javax.inject.Inject

class EditNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.editNote(note.copy(updateAt = System.currentTimeMillis()))
    }
}
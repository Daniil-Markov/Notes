package com.example.notepad.domain.usecase

import com.example.notepad.domain.entity.Note
import com.example.notepad.domain.repository.NoteRepository
import javax.inject.Inject

class EditNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.editNote(note.copy(updateAt = System.currentTimeMillis()))
    }
}
package com.example.notepad.domain.usecase

import com.example.notepad.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int){
        repository.deleteNote(noteId = noteId)
    }
}
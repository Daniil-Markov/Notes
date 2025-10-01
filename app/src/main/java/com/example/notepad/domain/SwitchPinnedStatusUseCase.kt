package com.example.notepad.domain

class SwitchPinnedStatusUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(noteId: Int){
        repository.switchPinnedStatus(noteId)
    }
}
package com.example.notepad.domain

import javax.inject.Inject

class SwitchPinnedStatusUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int){
        repository.switchPinnedStatus(noteId)
    }
}
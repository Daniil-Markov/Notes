package com.example.notepad.domain.usecase

import com.example.notepad.domain.repository.NoteRepository
import javax.inject.Inject

class SwitchPinnedStatusUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int){
        repository.switchPinnedStatus(noteId)
    }
}
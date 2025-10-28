package com.example.notepad.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
        private val repository: NoteRepository
    ) {
        operator fun invoke(): Flow<List<Note>>{
            return repository.getAllNotes()
        }

    }
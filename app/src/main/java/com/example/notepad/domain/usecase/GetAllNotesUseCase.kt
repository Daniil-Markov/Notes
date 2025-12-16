package com.example.notepad.domain.usecase

import com.example.notepad.domain.entity.Note
import com.example.notepad.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
        private val repository: NoteRepository
    ) {
        operator fun invoke(): Flow<List<Note>>{
            return repository.getAllNotes()
        }

    }
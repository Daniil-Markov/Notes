package com.example.notepad.domain.usecase

import com.example.notepad.domain.entity.ContentItem
import com.example.notepad.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(
        title: String,
        content: List<ContentItem>
    ) {
        repository.addNote(
            title = title,
            content = content,
            isPinned = false,
            updateAt = System.currentTimeMillis()
        )
    }
}
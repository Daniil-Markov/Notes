package com.example.notepad.domain.entity

data class Note(
    val noteId: Int,
    val title: String,
    val content: List<ContentItem>,
    val updateAt: Long,
    val isPinned: Boolean
)
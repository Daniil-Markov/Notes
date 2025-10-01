package com.example.notepad.domain

data class Note(
    val noteId: Int,
    val title: String,
    val content: String,
    val updateAt: Long,
    val isPinned: Boolean
)